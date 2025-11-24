import { Component, Output, EventEmitter, Input } from '@angular/core';
import { PostService } from '../post.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-create',
  standalone: true,
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css'],
  imports: [FormsModule, CommonModule]
})
export class CreatePostComponent {

  @Input() username: string = '';

  // 👇 NUEVO: Evento hacia el padre
  @Output() postCreated = new EventEmitter<void>();

  title = '';
  content = '';

  constructor(
    private postService: PostService,
    private router: Router
  ) { }
  maxLength = 280;
  contentLength = 0;

  onContentChange() {
    this.contentLength = this.content.trim().length;
  }


  showError = false;

  onSubmit() {
    this.showError = false;

    if (!this.title.trim() || !this.content.trim()) {
      this.showError = true;
      return;
    }

    if (this.contentLength > this.maxLength) {
      this.showError = true;
      return;
    }

    // Verificar campos vacíos
    if (!this.title.trim() || !this.content.trim()) {
      this.showError = true;
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      alert('Debes iniciar sesión para crear un post.');
      return;
    }

    const newPost = {
      title: this.title,
      content: this.content
    };

    this.postService.createPost(newPost, token).subscribe({
      next: () => {
        this.title = '';
        this.content = '';
        this.showError = false;
        this.postCreated.emit(); // refresh
      },
      error: (err) => {
        console.error('Error al crear post:', err);
        alert('❌ Error al crear el post.');
      }
    });
  }

}


