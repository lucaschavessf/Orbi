import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class UsuarioService {

  private usuarioKey = 'usuarioLogado';

  salvarUsuario(usuario: any) {
    localStorage.setItem(this.usuarioKey, JSON.stringify(usuario));
  }

  getUsuarioLogado() {
    const data = localStorage.getItem(this.usuarioKey);
    return data ? JSON.parse(data) : null;
  }

  limparSessao() {
    localStorage.removeItem(this.usuarioKey);
  }

  isLogado(): boolean {
    return !!this.getUsuarioLogado();
  }
}
