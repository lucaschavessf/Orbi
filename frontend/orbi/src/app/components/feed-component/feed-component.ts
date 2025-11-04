import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { Post, PostComponent } from '../post-component/post-component';
import { environment } from '../../../environments/environments';
import { CabecalhoService } from '../../services/cabecalho-service';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, HttpClientModule, PostComponent],
  templateUrl: './feed-component.html',
  styleUrls: ['./feed-component.css']
})
export class FeedComponent implements OnInit, OnDestroy {
  posts: Post[] = [];
  private subscription!: Subscription;

  constructor(private http: HttpClient, private searchService: CabecalhoService) {}

  ngOnInit(): void {
    this.subscription = this.searchService.searchTerm$.subscribe(term => {
      this.loadPosts(term);
    });

    // carregar inicialmente sem termo
    this.loadPosts('');
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  loadPosts(term: string) {
  let currentUrl = '';

  if (!term) {
    currentUrl = `${environment.apiUrl}/posts/listar_posts?size=10`;
  } else {
    currentUrl = `${environment.apiUrl}/api/busca?q=${encodeURIComponent(term)}`;
  }

  this.http.get<any>(currentUrl).subscribe({
    next: (response) => {
      const items = response.content ?? response;
      this.posts = items.map((item: any) => {
        const usernameAutor = item.usernameAutor ?? item.autor?.username ?? 'Desconhecido';

        return {
          id: item.id,
          titulo: item.titulo,
          conteudo: item.conteudo,
          usernameAutor,
          dataCriacao: item.dataCriacao,
          numeroCurtidas: item.totalCurtidas
        };
      });
    },
    error: (err) => console.error('Erro ao carregar posts:', err)
  });
}

}
