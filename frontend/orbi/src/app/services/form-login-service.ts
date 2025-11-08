import { inject, Injectable } from '@angular/core';
import { Usuario } from '../models/usuario';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class FormLoginService {
  private apiUrl = `${environment.apiUrl}/usuarios/login`;

  private http = inject(HttpClient);

  logar(usuario: Usuario): Observable<any> {
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }
}
