import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CommentService } from '../../services/comment-service';
import { UsuarioService } from '../../services/usuario-service';
import { Comment } from '../../models/comment';
import { ComentarioItemComponent } from '../comentario-item-component/comentario-item-component';

@Component({
  selector: 'app-post-comentario',
  standalone: true,
  imports: [CommonModule, FormsModule, ComentarioItemComponent],
  templateUrl: './post-comentario-component.html'
})
export class PostComentarioComponent {

  @Input() postId!: string;

  comentarios: Comment[] = [];
  novoComentario: string = '';

  private commentService = inject(CommentService);
  private usuarioService = inject(UsuarioService);

  ngOnInit() {
    this.carregarComentarios();
  }

  carregarComentarios() {
    const username = this.usuarioService.getUsuarioLogado().username;

    this.commentService.listarComentarios(this.postId, username)
      .subscribe(res => this.comentarios = res);
  }

  enviarComentario() {
    if (!this.novoComentario.trim()) return;

    const username = this.usuarioService.getUsuarioLogado().username;

    this.commentService
      .criarComentario(this.postId, this.novoComentario, username)
      .subscribe(novo => {
        this.comentarios.push(novo);
        this.novoComentario = '';
      });
  }
}

