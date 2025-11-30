export interface Usuario {
  username: string;
  nome: string;
  email: string;
  senha: string;
  tipo: 'ALUNO' | 'PROFESSOR';
  curso?: string;
  bio?: string;
  fotoPerfil?: string;
}
