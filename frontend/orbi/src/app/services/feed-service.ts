import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environments';
import { Post } from '../components/post-component/post-component';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FeedService {
  private http = inject(HttpClient);

  getPosts(username: string, page: number, size: number = 10): Observable<Post[]> {
    const url = `${environment.apiUrl}/posts?username=${username}&page=${page}&size=${size}`;
    return this.http.get<any>(url).pipe(
      mapResponseToPosts()
    );
  }

  searchPosts(username: string, term: string): Observable<Post[]> {
    const url = `${environment.apiUrl}/posts/search?username=${username}&q=${encodeURIComponent(term)}`;
    return this.http.get<any>(url).pipe(
      mapResponseToPosts()
    );
  }

  getFavoritos(username: string): Observable<Post[]> {
    const url = `${environment.apiUrl}/posts/favoritos?username=${username}`;
    return this.http.get<any>(url).pipe(
      mapResponseToPosts()
    );
  }

  getPostsByUsername(username: string, page: number, size: number = 10): Observable<Post[]> {
    const url = `${environment.apiUrl}/posts/usuario?username=${username}&page=${page}&size=${size}`
    return this.http.get<Post[]>(url).pipe(
      mapResponseToPosts()
    );
  }

}

function mapResponseToPosts() {
  return map((response: any) => {
    const items = response.content ?? response;
    return items.map((item: any) => {
      const usernameAutor = item.usernameAutor ?? item.autor?.username ?? 'Desconhecido';
      return {
        id: item.id,
        titulo: item.titulo,
        conteudo: item.conteudo,
        usernameAutor,
        dataCriacao: item.dataCriacao,
        numeroCurtidas: item.totalCurtidas,
        numeroDeslikes: item.totalDeslikes,
        totalPontuacao: (item.totalCurtidas ?? 0) - (item.totalDeslikes ?? 0),
        curtidoPeloUsuario: item.curtidoPeloUsuario,
        descurtidoPeloUsuario: item.descurtidoPeloUsuario,
        favoritadoPeloUsuario: item.favoritadoPeloUsuario,
        urlArquivo: item.urlArquivo,
        totalComentarios: item.totalComentarios,
        editado: item.editado,
        dataEdicao: item.dataHoraEdicao
      };
    });
  });
}
