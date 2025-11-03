import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Post, PostComponent } from '../post-component/post-component';
import { environment } from '../../../environments/environments';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, HttpClientModule, PostComponent],
  templateUrl: './feed-component.html',
  styleUrl: './feed-component.css'
})
export class FeedComponent implements OnInit {
  posts: Post[] = [];
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any>(`${environment.apiUrl}/posts/listar_posts?size=10`).subscribe({
      next: (response) => {
        this.posts = response.content.map((item: any) => ({
          id: item.id,
          titulo: item.titulo,
          conteudo: item.conteudo,
          usernameAutor: item.usernameAutor,
          dataCriacao: item.dataCriacao,
          numeroCurtidas: item.numeroCurtidas
        }));
      },
      error: (err) => console.error('Erro ao carregar posts:', err)
    });
  }
}
