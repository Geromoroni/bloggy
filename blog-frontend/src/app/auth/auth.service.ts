import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../config/enviroment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  
  isLoggedIn(): boolean {
  return !!localStorage.getItem('token');
}
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password });
  }

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { username, email, password });
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
  }
 getCurrentUser(): Observable<{ id: number; username: string; email: string }> {
  const token = localStorage.getItem('token');
  return this.http.get<{ id: number; username: string; email: string }>(
    `${this.apiUrl}/me`,
    {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`,
      }),
    }
  );
}


}
