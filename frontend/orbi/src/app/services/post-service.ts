import { inject, Injectable } from '@angular/core';
import { Post } from '../models/posts';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrlCriarPost = `${environment.apiUrl}/posts`;
  private http = inject(HttpClient);

  postar(post: Post): Observable<any> {
    return this.http.post<Post>(this.apiUrlCriarPost, post);
  }

  curtirPost(postId: string, username: string): Observable<any> {
    console.log('Curtindo post com ID:', postId, 'para o usuário:', username);
    const url = `${environment.apiUrl}/posts/${postId}/avaliar?avaliacao=True&username=${username}`;
    return this.http.post(url, null,{ responseType: 'text' });
  }
  descurtirPost(postId: string, username: string): Observable<any> {
    console.log('Curtindo post com ID:', postId, 'para o usuário:', username);
    const url = `${environment.apiUrl}/posts/${postId}/avaliar?avaliacao=False&username=${username}`;
    return this.http.post(url, null,{ responseType: 'text' });
  }

  favoritarPost(postId: string, username: string): Observable<any> {
    console.log('Curtindo post com ID:', postId, 'para o usuário:', username);
    const url = `${environment.apiUrl}/posts/${postId}/favoritar?username=${username}`;
    return this.http.post(url, null,{ responseType: 'text' });
  }

  obterPostPorId(postId: string, username: string): Observable<Post> {
    const url = `${environment.apiUrl}/posts/${postId}?username=${username}`;
    return this.http.get<Post>(url);
  }
  atualizarPost(postId: string, post: any): Observable<Post>{
    const url = `${environment.apiUrl}/posts/${postId}`;
    return this.http.put<Post>(url,post);
  }

  excluirPost(postId: string): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/posts/${postId}`);
  }

}

