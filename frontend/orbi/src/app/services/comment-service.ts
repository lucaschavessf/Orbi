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


  criarComentario(postId: string, conteudo: string, usernameAutor: string, comentarioPaiId: string | null = null): Observable<Comment> {
    const body = {
      usernameAutor,
      conteudo,
      postId,
      comentarioPaiId
    };

    return this.http.post<Comment>(this.apiUrl, body);
  }

  criarResposta(comentarioId: string, conteudo: string, username: string) {
    return this.http.post<Comment>(
      `/api/comentarios/${comentarioId}/respostas`,
      { conteudo, username }
    );
  }

}
