import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { Post, PostComponent } from '../post-component/post-component';
import { CabecalhoService } from '../../services/cabecalho-service';
import { UsuarioService } from '../../services/usuario-service';
import { FeedService } from '../../services/feed-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, PostComponent], 
  templateUrl: './feed-component.html',
  styleUrls: ['./feed-component.css']
})
export class FeedComponent implements OnInit, OnDestroy {
  posts: Post[] = [];
  private subscription!: Subscription;
  private usuarioService = inject(UsuarioService); 
  private feedService = inject(FeedService);
  private router = inject(Router);
  private usernameAutor = this.usuarioService.getUsuarioLogado().username;
  constructor(private searchService: CabecalhoService) {}

  ngOnInit(): void {
    this.subscription = this.searchService.searchTerm$.subscribe(term => {
      this.loadPosts(term);
    });
    this.loadPosts('');
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  loadPosts(term: string) {
    if (this.router.url.startsWith('/favoritos')) {
      this.feedService.getFavoritos(this.usernameAutor).subscribe({
        next: (posts) => {
          this.posts = posts;
        },
        error: (err) => console.error('Erro ao carregar favoritos:', err)
      });
      return;
    }

    if (!term) {
      this.feedService.getPosts(this.usernameAutor).subscribe({
        next: (posts) => {
          this.posts = posts;
        },
        error: (err) => console.error('Erro ao carregar posts:', err)
      });
    } else {
      this.feedService.searchPosts(this.usernameAutor, term).subscribe({
        next: (posts) => {
          this.posts = posts;
        },
        error: (err) => console.error('Erro ao carregar posts:', err)
      });
    }
  }
}
