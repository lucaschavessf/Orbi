import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { provideHttpClient, withInterceptors } from '@angular/common/http'; 
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { authInterceptor } from './app/interceptors/auth.interceptor'; 
import { provideZoneChangeDetection } from '@angular/core';

bootstrapApplication(App, {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    
    provideHttpClient(
      withInterceptors([authInterceptor])
    ),

    provideRouter(routes)
  ]
}).catch(err => console.error(err));