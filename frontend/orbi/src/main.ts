import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app'; // Certifique-se de que o caminho está correto
import { provideHttpClient } from '@angular/common/http';

bootstrapApplication(App, {
  providers: [
    // ESSA LINHA RESOLVE O ERRO NG0201
    provideHttpClient()
    // Adicione outros provedores globais aqui
  ]
}).catch(err => console.error(err));