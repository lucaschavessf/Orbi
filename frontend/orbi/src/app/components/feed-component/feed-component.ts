import { Component, OnInit, OnDestroy, inject, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { Post, PostComponent } from '../post-component/post-component';
import { CabecalhoService } from '../../services/cabecalho-service';
import { UsuarioService } from '../../services/usuario-service';
import { FeedService } from '../../services/feed-service';
import { Router } from '@angular/router';
import { AzureService } from '../../services/azure-service';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, PostComponent], 
  templateUrl: './feed-component.html',
  styleUrls: ['./feed-component.css']
})
export class FeedComponent implements OnInit, OnDestroy {
  posts: Post[] = [];
  page = 0;
  loading = false;
  noMorePosts = false;
  private subscription!: Subscription;
  private usuarioService = inject(UsuarioService); 
  private feedService = inject(FeedService);
  private azureService = inject(AzureService);
  private router = inject(Router);
  private usernameAutor = this.usuarioService.getUsuarioLogado().username;
  private currentTerm: string = '';
  constructor(private searchService: CabecalhoService) {}

ngOnInit(): void {
    this.subscription = this.searchService.searchTerm$.subscribe(term => {
      this.currentTerm = term;
      this.page = 0;
      this.noMorePosts = false;
      this.posts = [];

      if(term != ''){
        this.loadPosts(term);
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private processPosts(posts: Post[]): Post[] {
    return posts.map(post => {
      if (post.urlArquivo) { 
        const fileName = post.urlArquivo.split('/').pop();
        if (fileName) {
          const containerName = 'imagens';
          this.azureService.generateReadUrl(containerName, fileName).subscribe({
            next: (resp: any) => {
              post.urlArquivo = resp?.uploadUrl ?? resp?.url ?? null;
            },
            error: (err: any) => {
              console.error('Erro ao gerar URL da imagem:', err);
            }
          });
        }
      }
      return post;
    });
  }

  loadPosts(term: string) {
    if (this.router.url.startsWith('/favoritos')) {
      this.feedService.getFavoritos(this.usernameAutor).subscribe({
        next: (posts) => {
          this.posts = this.processPosts(posts);
        },
        error: (err) => console.error('Erro ao carregar favoritos:', err)
      });
      return;
    }
    if (!term) {
      this.feedService.getPosts(this.usernameAutor, this.page)
        .subscribe({
          next: (novosPosts) => {
            if (novosPosts.length === 0) {
              this.noMorePosts = true;
              return;
            }
            novosPosts = this.processPosts(novosPosts);
            this.posts = [...this.posts, ...novosPosts];
            this.page++;
          },
          complete: () => this.loading = false
        });
    } else {
      this.feedService.searchPosts(this.usernameAutor, term).subscribe({
        next: (posts) => {
          this.posts = [];
          console.log(4,this.posts.length) 
          this.posts = this.processPosts(posts);
        },
        error: (err) => console.error('Erro ao carregar posts:', err)
      });
    }
  }

  @ViewChild('sentinel', { static: true }) sentinel!: ElementRef;

  ngAfterViewInit() {
    const observer = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && this.currentTerm.trim() == '') {
        this.loadPosts(this.currentTerm);
      }
    });

    observer.observe(this.sentinel.nativeElement);
  }
}
