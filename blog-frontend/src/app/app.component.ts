import { Component } from '@angular/core';
import { RouterModule, Router, RouterOutlet } from '@angular/router';
import { AuthService } from './auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(public auth: AuthService, private router: Router) {

  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
