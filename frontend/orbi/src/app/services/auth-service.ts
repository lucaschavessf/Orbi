import { Injectable } from '@angular/core';
import { Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly USER_KEY = 'usuario_logado';

  salvarUsuario(usuario: Usuario): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(usuario));
  }

  obterUsuario(): Usuario | null {
    const dados = localStorage.getItem(this.USER_KEY);
    return dados ? JSON.parse(dados) : null;
  }

  estaLogado(): boolean {
    return !!this.obterUsuario();
  }

  logout(): void {
    localStorage.removeItem(this.USER_KEY);
  }
}
