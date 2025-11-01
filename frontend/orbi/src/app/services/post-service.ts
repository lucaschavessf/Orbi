import { inject, Injectable } from '@angular/core';
import { Post } from '../models/posts';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = `${environment.apiUrl}/posts/criar_post`;
  private http = inject(HttpClient);

  postar(post: Post): Observable<any> {
    return this.http.post<Post>(this.apiUrl, post);
  }
}

