import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  // Obtener todos los posts públicos
  getAllPosts(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // ✅ Crear un nuevo post (requiere token)
  createPost(post: any, token: string) {
  const headers = {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };

  return this.http.post(`${this.apiUrl}`, post, { headers });
}

  // Eliminar post (requiere token)
  deletePost(id: number) {
  const token = localStorage.getItem('token');
  return this.http.delete(`${this.apiUrl}/${id}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
}


  // Obtener post por ID
  getPostById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
  // Actualizar post (requiere token)
  updatePost(id: number, post: any) {
  const token = localStorage.getItem('token');
  return this.http.put(`${this.apiUrl}/${id}`, post, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
}

getCurrentUser() {
  const token = localStorage.getItem('token');
  return this.http.get<any>(`${this.apiUrl}/auth/me`, {
    headers: { Authorization: `Bearer ${token}` }
  });
}

}
