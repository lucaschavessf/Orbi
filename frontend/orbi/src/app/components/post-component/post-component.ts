import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario-service'; 
import { PostService } from '../../services/post-service';

export interface Post {
  id: string;
  titulo: string;
  conteudo: string;
  usernameAutor: string;
  dataCriacao: string;
  numeroCurtidas?: number;
  comments?: number;
  flair?: string;
}

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-component.html',
  styleUrl: './post-component.css'
})
export class PostComponent {
  @Input() post!: Post;
  private usuarioService = inject(UsuarioService); 
  private postService = inject(PostService);
  private usernameAutor = this.usuarioService.getUsuarioLogado().username;

  formatVotes(votes: number = 0): string {
    if (votes >= 1000) return (votes / 1000).toFixed(1) + 'k';
    return votes.toString();
  }

  curtirPost(): void {
    this.postService.curtirPost(this.post.id,this.usernameAutor).subscribe({
      next: (res: string) => {
        console.log('Post curtido com sucesso:', res);
      },
      error: (err) => {
        console.error('Erro ao curtir Post', err);
      }
    });
  }
  descurtirPost(): void {
    this.postService.descurtirPost(this.post.id,this.usernameAutor).subscribe({
      next: (res: string) => {
        console.log('Post curtido com sucesso:', res);
      },
      error: (err) => {
        console.error('Erro ao curtir Post', err);
      }
    });
  }

}
