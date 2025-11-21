import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private apiUrl = 'http://localhost:8080/comentarios';

  constructor(private http: HttpClient) {}

  listarComentarios(postId: string, username: string): Observable<Comment[]> {
  return this.http.get<Comment[]>(`${this.apiUrl}/post/${postId}?username=${username}`);
  }


  criarComentario(postId: string, conteudo: string, usernameAutor: string): Observable<Comment> {
    const body = {
      usernameAutor,
      conteudo,
      postId,
      comentarioPaiId: null
    };

    return this.http.post<Comment>(this.apiUrl, body);
  }
}
