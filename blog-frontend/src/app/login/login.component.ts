import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  styleUrls: ['./login.component.css'],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';
  error = '';
  loading =false;
  showPassword= false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/posts']);
    }
  }
onLogin(form: NgForm): void {
  this.error = '';

  if (form.invalid) return;

  this.loading = true;

  this.authService.login(this.email, this.password).subscribe({
    next: (res) => {
      this.authService.saveToken(res.token);
      this.router.navigate(['/posts']);
    },
    error: (err) => {
      this.loading = false;

      this.error =
        err.status === 401
          ? 'Credenciales inválidas.'
          : 'Error al iniciar sesión.';

      // QUITAR EL SHAKE DESPUÉS DE 1.5s
      setTimeout(() => (this.error = ''), 1500);
    },
  });
}

}
