// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { FormCadastroComponent } from './components/form-cadastro-component/form-cadastro-component';
import { FormLoginComponent } from './components/form-login-component/form-login-component';

export const routes: Routes = [
  { path: '', component: FormCadastroComponent },
  { path: 'login', component: FormLoginComponent }
];
