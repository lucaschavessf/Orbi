import { inject, Injectable } from '@angular/core';
import { Usuario } from '../models/usuario';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FormLoginService {
  private apiUrl = 'http://localhost:8080/cadastrar_usuario';

  // Injeção de dependência moderna usando 'inject'
  private http = inject(HttpClient);

  logar(usuario: Usuario): Observable<any> {
    // Retorna a chamada POST para a sua API
    return of({ status: 200, message: 'OK' });
  }
}
