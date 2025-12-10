import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario';
import { environment } from '../../environments/environments';

@Injectable({ providedIn: 'root' })
export class UsuarioService {

  private usuarioKey = 'usuarioLogado';
  private apiUrl = `${environment.apiUrl}/usuarios`;
  private http = inject(HttpClient);

  salvarUsuario(usuario: any) {
    localStorage.setItem(this.usuarioKey, JSON.stringify(usuario));
  }

  getUsuarioLogado() {
    const data = localStorage.getItem(this.usuarioKey);
    return data ? JSON.parse(data) : null;
  }

  getUsuario(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${username}`);
  }

  atualizarPerfil(username: string, body: any) {
    return this.http.put(`${this.apiUrl}/perfil/${username}`, body);
  }


  limparSessao() {
    localStorage.removeItem(this.usuarioKey);
  }

  isLogado(): boolean {
    return !!this.getUsuarioLogado();
  }
}
