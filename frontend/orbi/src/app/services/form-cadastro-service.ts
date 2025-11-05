import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class FormCadastroService {

  private apiUrl = `${environment.apiUrl}/cadastrar_usuario`;;

  private http = inject(HttpClient);

  cadastrar(usuario: Usuario): Observable<any> {
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }
}
