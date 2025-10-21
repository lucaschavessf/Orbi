import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class FormCadastroService {

  // URL da sua API Spring
  private apiUrl = 'http://localhost:8080/cadastrar_usuario';

  // Injeção de dependência moderna usando 'inject'
  private http = inject(HttpClient);

  cadastrar(usuario: Usuario): Observable<any> {
    // Retorna a chamada POST para a sua API
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }
}
