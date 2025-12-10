import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Comment } from '../../models/comment';
import { CommentService } from '../../services/comment-service';
import { UsuarioService } from '../../services/usuario-service';

@Component({
  selector: 'app-comentario-item',
  standalone: true,
  imports: [CommonModule, FormsModule, NgForOf, NgIf],
  templateUrl: './comentario-item-component.html'
})
export class ComentarioItemComponent {

  @Input() comentario!: Comment;
  @Input() postId!: string;
  @Input() nivel: number = 0;
  @Output() deletado = new EventEmitter<string>();

  mostrandoRespostas = false;
  mostrandoInputResposta = false;
  respostaTexto = '';

  private commentService = inject(CommentService);
  private usuarioService = inject(UsuarioService);

  toggleRespostas() {
    this.mostrandoRespostas = !this.mostrandoRespostas;
  }

  toggleResponder() {
    this.mostrandoInputResposta = !this.mostrandoInputResposta;
  }

  getTotalRespostas(comentario: Comment): number {
    if (!comentario.respostas || comentario.respostas.length === 0) return 0;

    let total = comentario.respostas.length;

    for (let r of comentario.respostas) {
      total += this.getTotalRespostas(r);
    }

    return total;
  }

  enviarResposta() {
    if (!this.respostaTexto.trim()) return;

    const username = this.usuarioService.getUsuarioLogado().username;

    this.commentService
      .criarComentario(this.postId, this.respostaTexto, username, this.comentario.id)
      .subscribe(nova => {
        this.comentario.respostas.push(nova);
        this.respostaTexto = '';
        this.mostrandoInputResposta = false;
        this.mostrandoRespostas = true;
      });
  }

  avaliar(avaliacao: boolean) {
    const username = this.usuarioService.getUsuarioLogado().username;

    this.commentService
      .avaliarComentario(this.comentario.id, avaliacao, username)
      .subscribe(res => {

        this.comentario.totalCurtidas   = res.totalCurtidas;
        this.comentario.totalDeslikes   = res.totalDeslikes;
        this.comentario.curtido         = res.curtido;
        this.comentario.descurtido      = res.descurtido;
      });
  }

deletarComentario() {
  const username = this.usuarioService.getUsuarioLogado().username;

  this.commentService
    .deletarComentario(this.comentario.id, username)
    .subscribe(() => {
      this.deletado.emit(this.comentario.id);
    });
}

isAutor(): boolean {
  return this.comentario.usernameAutor === this.usuarioService.getUsuarioLogado().username;
}

}
