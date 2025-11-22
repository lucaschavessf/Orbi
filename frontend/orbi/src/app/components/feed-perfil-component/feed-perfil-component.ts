import { Component, Input, OnInit, inject } from '@angular/core';
import { FeedService } from '../../services/feed-service';
import { AzureService } from '../../services/azure-service';
import { Post, PostComponent} from '../post-component/post-component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-feed-perfil',
  standalone: true,
  imports: [CommonModule, PostComponent],
  templateUrl: './feed-perfil-component.html',
  styleUrls: ['./feed-perfil-component.css']
})
export class FeedPerfilComponent implements OnInit {

  @Input() username!: string;
  posts: Post[] = [];
  page = 0;
  private feedService = inject(FeedService);
  private azureService = inject(AzureService);
  loading = false;
  noMorePosts = false;

  ngOnInit(): void {
    if (this.username) {
      this.loadPostsUsuario();
    }
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

  loadPostsUsuario() {
    this.feedService.getPostsByUsername(this.username, this.page)
        .subscribe({
          next: (novosPosts) => {
            if (novosPosts.length === 0) {
              this.noMorePosts = true;
              return;
            }
            novosPosts = this.processPosts(novosPosts);
            this.posts = [...this.posts, ...novosPosts];
            console.log('Posts carregados para', this.username, ':', novosPosts);
            this.page++;
          },
          complete: () => this.loading = false
        });
  }
}
