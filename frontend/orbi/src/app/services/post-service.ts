import { inject, Injectable } from '@angular/core';
import { Post } from '../models/posts';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrlCriarPost = `${environment.apiUrl}/posts/criar_post`;
  private apiUrlCurtirPost = `${environment.apiUrl}/posts/`;
  private http = inject(HttpClient);

  postar(post: Post): Observable<any> {
    return this.http.post<Post>(this.apiUrlCriarPost, post);
  }

curtirPost(postId: string, username: string): Observable<any> {
  console.log('Curtindo post com ID:', postId, 'para o usuário:', username);
  const url = `${environment.apiUrl}/posts/${postId}/curtir?username=${username}`;
  return this.http.post(url, null,{ responseType: 'text' });
}
descurtirPost(postId: string, username: string): Observable<any> {
  console.log('Curtindo post com ID:', postId, 'para o usuário:', username);
  const url = `${environment.apiUrl}/posts/${postId}/descurtir?username=${username}`;
  return this.http.post(url, null,{ responseType: 'text' });
}

}

