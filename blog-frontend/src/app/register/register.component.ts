import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: `register.component.html`,
  styleUrls: ['register.component.css']
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  onRegister(form: NgForm): void {
    this.error = '';

    // 👉 Validación extra por si el formulario está incompleto
    if (form.invalid) {
      this.error = 'Por favor completa todos los campos correctamente.';
      return;
    }

    this.authService.register(this.username, this.email, this.password).subscribe({
      next: () => {
        alert('✅ Registro exitoso, ahora podés iniciar sesión');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        if (err.error?.error?.includes('email')) {
          this.error = 'Este email ya está en uso.';
        } else {
          this.error = 'Error al registrarse. Verifica los datos.';
        }
      }
    });
  }
}
