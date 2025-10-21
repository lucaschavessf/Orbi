export interface Usuario {
  username: string;
  nome: string;
  cpf?: string;
  email: string;
  senha: string;
  tipo: 'ALUNO' | 'PROFESSOR';
  curso?: string;
  bio?: string;
}
