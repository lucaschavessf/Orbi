import { Routes } from '@angular/router';
import { FormCadastroComponent } from './components/form-cadastro-component/form-cadastro-component';
import { FormLoginComponent } from './components/form-login-component/form-login-component';
import { HomeComponent } from './components/home-component/home-component';
import { LayoutPrincipalComponent } from './components/layout-principal-component/layout-principal-component';
import { FeedComponent } from './components/feed-component/feed-component';
import { CriarPostComponent } from './components/criar-post-component/criar-post-component';
import { AuthGuard } from './guards/auth-guard-guard';
import { ChatAssistentComponent } from './components/chat-assistent-component/chat-assistent-component';
import { PerfilComponent } from './components/perfil-component/perfil-component';
import { EditarPerfilComponent } from './components/editar-perfil-component/editar-perfil-component';
import { EditPostComponent } from './components/edit-post-component/edit-post-component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'cadastrar', component: FormCadastroComponent },
  { path: 'login', component: FormLoginComponent },

  {
    path: '',
    component: LayoutPrincipalComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'feed', component: FeedComponent },
      { path: 'favoritos', component: FeedComponent },
      { path: 'criar_post', component: CriarPostComponent },
      { path: 'chat_ai', component: ChatAssistentComponent },
      { path: 'editar', component: EditarPerfilComponent },
      { path: 'editar_post/:id', component: EditPostComponent },


      {
        path: 'perfil',
        children: [
          { path: ':username', component: PerfilComponent }
        ]
      },

      { path: '', redirectTo: 'feed', pathMatch: 'full' }
    ]
  },

  { path: '**', redirectTo: '' }
];
