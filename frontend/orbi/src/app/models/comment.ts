export interface Comment {
  id: string;
  conteudo: string;
  dataCriacao: string;

  usernameAutor: string;
  nomeAutor: string;
  fotoPerfilAutor: string;

  totalCurtidas: number;
  totalDeslikes: number;

  curtido: boolean;
  descurtido: boolean;

  comentarioPaiId?: string;

  respostas: Comment[];
}
