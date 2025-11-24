import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PostService } from '../post.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule]
})
export class EditPostComponent {

  @Input() post: any = { id: 0, title: '', content: '' };

  @Output() close = new EventEmitter<void>();
  @Output() saved = new EventEmitter<void>();

  constructor(private postService: PostService) {}

  onSubmit(): void {
    this.postService.updatePost(this.post.id, this.post).subscribe({
      next: () => {
        alert('✅ Post actualizado correctamente');
        this.saved.emit();   // <--- avisar al padre
      },
      error: (err) => console.error('Error al actualizar post:', err)
    });
  }

  closeModal() {
    this.close.emit();
  }
}
