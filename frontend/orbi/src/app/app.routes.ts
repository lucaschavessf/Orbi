// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { FormCadastroComponent } from './components/form-cadastro-component/form-cadastro-component';
import { FormLoginComponent } from './components/form-login-component/form-login-component';
import { HomeComponent } from './components/home-component/home-component';
import { LayoutPrincipalComponent } from './components/layout-principal-component/layout-principal-component';

export const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'cadastrar', component: FormCadastroComponent },
  { path: 'login', component: FormLoginComponent },
  { path: 'feed', component: LayoutPrincipalComponent}
];
