import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

// Interface para definir a estrutura de um post
interface Post {
  id: number;
  community: string;
  communityIcon: string; // URL ou path para o ícone
  author: string;
  time: string;
  title: string;
  content: string;
  upvotes: number;
  comments: number;
  pinned?: boolean; // Para posts fixados
  flair?: string; // Tag/Flair
}

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule], // Apenas CommonModule é necessário para *ngFor
  templateUrl: './feed-component.html',
  styleUrl: './feed-component.css'
})
export class FeedComponent {

  // Dados estáticos de exemplo para o feed
  posts: Post[] = [
    {
      id: 1,
      community: 'r/datasciencebr',
      communityIcon: 'https://placehold.co/24x24/7c3aed/white?text=DS', // Placeholder
      author: 'u/Nice-Palpitation5589',
      time: 'há 1 h',
      title: 'Indicação de IA',
      flair: 'Dúvida',
      content: 'Pessoal, boa noite! Trabalho em uma empresa bem grande de tintas domiciliar. Tive uma reunião com a diretoria e dei uma ideia de programar um software executável no computador. Esse software é um programa onde todos os gerentes de lojas terão acesso e nele poderão abrir um ticket relatando o problema que está tendo em sua loja (computador/impressora e etc.) e eu quero uma ajuda de vocês para me indicar alguma IA para me ajudar nesse processo de desenvolvimento deste software, gostaria de saber uma IA que é muito boa para isso, poderiam me ajudar? pode ser tanto paga quanto gratuita!! Obrigado.',
      upvotes: 2,
      comments: 4
    },
    {
      id: 2,
      community: 'r/DistantHorizons',
      communityIcon: 'https://placehold.co/24x24/22c55e/white?text=DH', // Placeholder
      author: 'u/MinecraftModder',
      time: 'há 5 h',
      title: 'Novo update de performance!',
      flair: 'Notícia',
      content: 'Acabamos de lançar a versão 2.0 com melhorias significativas de otimização de chunks. Agora o mod roda ainda mais liso em computadores mais modestos. Confiram o changelog completo no nosso site e deixem o feedback!',
      upvotes: 128,
      comments: 22
    },
    {
      id: 3,
      community: 'r/programacao',
      communityIcon: 'https://placehold.co/24x24/3b82f6/white?text=P', // Placeholder
      author: 'u/DevJunior',
      time: 'há 2 dias',
      title: 'Qual a melhor linguagem para começar em 2025?',
      flair: 'Carreira',
      content: 'Estou querendo migrar de área e entrar no mundo da programação. Vejo muita gente falando de Python, outros de JavaScript... O que vocês, que já estão na área, recomendam para quem está começando do zero absoluto?',
      upvotes: 76,
      comments: 59
    }
  ];

  // Dados para o post fixado (simulando o "LESSONS #2" da imagem)
  pinnedPost = {
    title: 'DATASCIECEBR LESSONS #2 - Estatística Básica',
    votes: 33,
    comments: 6,
    imageUrl: 'https://placehold.co/100x70/334155/e2e8f0?text=Thumb' // Placeholder
  };

  constructor() { }

  // Funções de formatação (exemplo)
  formatVotes(votes: number): string {
    if (votes > 1000) {
      return (votes / 1000).toFixed(1) + 'k';
    }
    return votes.toString();
  }
}
