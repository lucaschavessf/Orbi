import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app'; // Certifique-se de que o caminho está correto
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';

bootstrapApplication(App, {
  providers: [
    // ESSA LINHA RESOLVE O ERRO NG0201
    provideHttpClient(),
    provideRouter(routes)
    // Adicione outros provedores globais aqui
  ]
}).catch(err => console.error(err));