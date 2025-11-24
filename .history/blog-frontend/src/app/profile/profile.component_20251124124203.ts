import { Component, type OnInit } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterModule, ActivatedRoute, Router } from "@angular/router"
import { PostService } from "../posts/post.service"
import { AuthService } from "../auth/auth.service"


@Component({
  selector: "app-profile",
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"],
})
export class ProfileComponent implements OnInit {
  username = ""
  userPosts: any[] = []
  currentUserId: number | null = null
  isOwnProfile = false
  postCount = 0

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    // Obtener el username de la URL
    this.route.params.subscribe((params) => {
      this.username = params["username"]
      this.loadUserPosts()
    })

    // Obtener el usuario actual
    const token = localStorage.getItem("token")
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split(".")[1]))
        this.currentUserId = payload.id || null
        const currentUsername = payload.username || ""
        this.isOwnProfile = currentUsername === this.username
      } catch (e) {
        console.error("Error al decodificar el token:", e)
      }
    }
  }

  loadUserPosts(): void {
    // Cargar todos los posts y filtrar por username
    this.postService.getAllPosts().subscribe({
      next: (posts) => {
        this.userPosts = posts.filter((post) => post.authorUsername === this.username)
        this.postCount = this.userPosts.length
      },
      error: (err) => console.error("Error al cargar posts del usuario:", err),
    })
  }

  deletePost(postId: number): void {
    if (confirm("¿Seguro que deseas eliminar este post?")) {
      this.postService.deletePost(postId).subscribe({
        next: () => {
          this.loadUserPosts()
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

}
