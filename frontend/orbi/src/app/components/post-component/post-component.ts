import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario-service'; 
import { PostService } from '../../services/post-service';

export interface Post {
  descurtidoPeloUsuario: boolean;
  curtidoPeloUsuario: boolean;
  favoritadoPeloUsuario?: boolean;
  id: string;
  titulo: string;
  conteudo: string;
  usernameAutor: string;
  dataCriacao: string;
  numeroCurtidas?: number;
  totalPontuacao?: number;
  numeroDeslikes?: number;
  comments?: number;
  flair?: string;
  urlArquivo?: string;
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
  selectedImage: string | null = null;

  formatVotes(votes: number = 0): string {
    if (votes >= 1000) return (votes / 1000).toFixed(1) + 'k';
    return votes.toString();
  }

  favoritarPost(): void {
    console.log(this.post.favoritadoPeloUsuario);
    this.postService.favoritarPost(this.post.id, this.usernameAutor).subscribe({
      next: () => {
        if(this.post.favoritadoPeloUsuario == false){
          this.post.favoritadoPeloUsuario = true;    
        }
        else{
          this.post.favoritadoPeloUsuario = false;     
        }
      }
    });
  }

  curtirPost(): void {
    console.log(this.post.curtidoPeloUsuario);
    this.postService.curtirPost(this.post.id, this.usernameAutor).subscribe({
      next: () => {
        if(this.post.curtidoPeloUsuario == false){
          this.post.numeroCurtidas = (this.post.numeroCurtidas ?? 0) + 1;
          if(this.post.descurtidoPeloUsuario){
            this.post.numeroDeslikes = (this.post.numeroDeslikes ?? 0) - 1;
            this.post.descurtidoPeloUsuario = false;     
          }
          this.post.totalPontuacao = (this.post.numeroCurtidas ?? 0) - (this.post.numeroDeslikes ?? 0);
          this.post.curtidoPeloUsuario = true;     
        }
        else{
          this.post.numeroCurtidas = (this.post.numeroCurtidas ?? 0) - 1;
          this.post.totalPontuacao = (this.post.numeroCurtidas ?? 0) - (this.post.numeroDeslikes ?? 0);
          this.post.curtidoPeloUsuario = false;     
        }
      }
    });
  }
  descurtirPost(): void {
    console.log(this.post.descurtidoPeloUsuario);
    this.postService.descurtirPost(this.post.id,this.usernameAutor).subscribe({
      next: (res: string) => {
        if(this.post.descurtidoPeloUsuario == false){
          this.post.numeroDeslikes = (this.post.numeroDeslikes ?? 0) + 1;
          if(this.post.curtidoPeloUsuario){
            this.post.numeroCurtidas = (this.post.numeroCurtidas ?? 0) - 1;
            this.post.curtidoPeloUsuario = false;     
          }
          this.post.totalPontuacao = (this.post.numeroCurtidas ?? 0) - (this.post.numeroDeslikes ?? 0);
          this.post.descurtidoPeloUsuario = true;     
        }
        else{
          this.post.numeroDeslikes = (this.post.numeroDeslikes ?? 0) - 1;
          this.post.totalPontuacao = (this.post.numeroCurtidas ?? 0) - (this.post.numeroDeslikes ?? 0);
          this.post.descurtidoPeloUsuario = false;     
        }
      },
      error: (err) => {
        console.error('Erro ao curtir Post', err);
      }
    });
  }

}
