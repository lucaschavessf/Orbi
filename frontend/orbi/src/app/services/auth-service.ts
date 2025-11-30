import { Injectable } from '@angular/core';
import { Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private tokenKey = 'auth_token'; 
  private usuarioKey = 'usuarioLogado_auth'; 

  constructor() { }

  salvarToken(token: string): void {
    if (token) {
      localStorage.setItem(this.tokenKey, token);
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  salvarUsuario(usuario: Usuario): void {
    localStorage.setItem(this.usuarioKey, JSON.stringify(usuario));
  }

  getUsuario(): Usuario | null {
    const data = localStorage.getItem(this.usuarioKey);
    return data ? JSON.parse(data) : null;
  }

  limparSessao(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.usuarioKey);
  }

  estaLogado(): boolean {
    return !!this.getToken();
  }

  logout() {
    localStorage.removeItem('usuarioLogado');
    localStorage.removeItem('token');
  }
}