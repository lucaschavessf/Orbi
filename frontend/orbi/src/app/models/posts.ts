export interface Post {
    titulo: string;
    conteudo: string;
    usernameAutor: string;
    curtidoPeloUsuario?: boolean;
    descurtidoPeloUsuario?: boolean;
    urlArquivo?: string;
}
