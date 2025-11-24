import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PostService } from './post.service';
import { AuthService } from '../auth/auth.service';
import { CreatePostComponent } from "./create-post/create-post.component"; 
import { FormsModule } from '@angular/forms';
import {EditPostComponent} from './edit-post/edit-post.component'

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [CommonModule, RouterModule, CreatePostComponent, FormsModule, EditPostComponent],
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css']
})
export class PostListComponent implements OnInit {
  posts: any[] = [];
  isLoggedIn = false;
  currentUserId: number | null = null;
username: string = '';
  currentUsername = "";


  constructor(
    private postService: PostService,
    private authService: AuthService 
  ) {}

  
//MODAL PARA ELIMINAR UN POST.
postToDelete: any = null;

openDeleteModal(post: any) {
  this.postToDelete = post;
}

closeDeleteModal() {
  this.postToDelete = null;
}

confirmDelete() {
  if (!this.postToDelete) return;

  this.postService.deletePost(this.postToDelete.id).subscribe({
    next: () => {
      this.closeDeleteModal();
      this.loadPosts(); // recargar posts
    },
    error: (err) => console.error("Error al eliminar post:", err),
  });
}

  
 ngOnInit(): void {
  this.loadPosts();

  const token = localStorage.getItem("token");
  if (!token) return;

  this.isLoggedIn = true;

  try {
    // Intentamos decodificar el token (solo para verificar si es válido)
    const payload = JSON.parse(atob(token.split(".")[1]));
    console.log("Payload decodificado:", payload);

    // SIEMPRE pedimos el usuario real al backend
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        console.log("👤 Usuario actual:", user);
        this.username = user.username;
        this.currentUserId = user.id;
      },
      error: (err) => {
        console.error("❌ Error obteniendo usuario actual:", err);
        this.username = "";
        this.currentUserId = null;
      }
    });

  } catch (err) {
    console.error("Error decodificando token:", err);
    this.username = "";
    this.currentUserId = null;
  }
}




  loadPosts(): void {
    this.postService.getAllPosts().subscribe({
      next: (data) => {
        console.log('✅ Posts recibidos:', data);
        this.posts = data;
      },
      error: (err) => console.error('Error al cargar posts:', err)
    });
  }

  deletePost(id: number): void {
    if (confirm('¿Seguro que deseas eliminar este post?')) {
      this.postService.deletePost(id).subscribe({
        next: () => {
          alert('🗑️ Post eliminado correctamente');
          this.loadPosts();
        },
        error: (err) => console.error('Error al eliminar post:', err)
      });
    }
  }

  logout() {
  this.authService.logout();
  window.location.href = '/login';
}editingPost: any = null;

openEditModal(post: any) {
  this.editingPost = { ...post }; // copia para no romper la lista en vivo
}

closeEditModal() {
  this.editingPost = null;
}

reloadAfterEdit() {
  this.loadPosts();
  this.closeEditModal();
}

}
