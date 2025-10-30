import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface Post {
  id: string;
  titulo: string;
  conteudo: string;
  usernameAutor: string;
  dataCriacao: string;
  upvotes?: number;
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

  formatVotes(votes: number = 0): string {
    if (votes >= 1000) return (votes / 1000).toFixed(1) + 'k';
    return votes.toString();
  }
}
