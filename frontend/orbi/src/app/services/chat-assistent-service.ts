import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatAssistentService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/assistant`;

  sendMessage(message: string): Observable<string> {
    return this.http.post(this.apiUrl, message, { responseType: 'text' });
  }
}
