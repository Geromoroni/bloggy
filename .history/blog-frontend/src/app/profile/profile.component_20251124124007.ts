import { Component, type OnInit } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterModule, ActivatedRoute, Router } from "@angular/router"
import { PostService } from "../posts/post.service"
import { AuthService } from "../auth/auth.service"
import { UserService, UserProfile } from "../posts/";

@Component({
  selector: "app-profile",
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"],
})
export class ProfileComponent implements OnInit {
  user: UserProfile | null = null; // Para guardar toda la info del usuario
  userPosts: any[] = []
  currentUserId: number | null = null
  isOwnProfile = false
  postCount = 0
  apiBaseUrl = 'http://localhost:8080'; // Base URL para construir las rutas de las imágenes

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private authService: AuthService,
    private userService: UserService // Inyectamos el nuevo servicio
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      const username = params["username"];
      if (username) {
        this.loadUserProfile(username);
        this.loadUserPosts(username);
      }
    });

    // Obtener el usuario actual
    const token = localStorage.getItem("token")
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split(".")[1]))
        this.currentUserId = payload.id || null
        const currentUsername = payload.username || ""
        // Comparamos con el username del perfil cargado
        if (this.user) {
          this.isOwnProfile = currentUsername === this.user.username;
        }
      } catch (e) {
        console.error("Error al decodificar el token:", e)
      }
    }
  }

  loadUserProfile(username: string): void {
    this.userService.getUserByUsername(username).subscribe({
      next: (userData) => {
        this.user = userData;
        // Una vez que tenemos el usuario, verificamos si es el perfil propio
        this.checkIfOwnProfile();
      },
      error: (err) => console.error("Error al cargar el perfil del usuario:", err),
    });
  }

  loadUserPosts(username: string): void {
    // Cargar todos los posts y filtrar por username
    this.postService.getAllPosts().subscribe({
      next: (posts) => {
        this.userPosts = posts.filter((post) => post.authorUsername === username)
        this.postCount = this.userPosts.length
      },
      error: (err) => console.error("Error al cargar posts del usuario:", err),
    })
  }

  deletePost(postId: number): void {
    if (confirm("¿Seguro que deseas eliminar este post?")) {
      this.postService.deletePost(postId).subscribe({
        next: () => { // Recargamos los posts del usuario actual
          if (this.user) this.loadUserPosts(this.user.username);
        },
        error: (err) => console.error("Error al eliminar post:", err),
      })
    }
  }

  logout(): void {
    this.authService.logout()
    this.router.navigate(["/login"])
  }

  goBack() {
  window.history.back();
}

  private checkIfOwnProfile(): void {
    const token = this.authService.getToken();
    if (token && this.user) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.isOwnProfile = payload.username === this.user.username;
      } catch (e) {
        this.isOwnProfile = false;
      }
    }
  }
}
