import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Usuario } from '../../models/usuario';
import { FormLoginService } from '../../services/form-login-service';
import { UsuarioService } from '../../services/usuario-service';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-form-login-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-login-component.html',
  styleUrl: './form-login-component.css'
})
export class FormLoginComponent {
  usuario: Usuario = { username: '', nome: '', cpf: '', email: '', senha: '', tipo: 'ALUNO', curso: '', bio: '' };

  statusMessage = signal<string | null>(null);
  isError = signal(false);
  isLoading = signal(false);

  statusClass = computed(() =>
    this.isError()
      ? 'bg-red-100 text-red-800 border border-red-300'
      : 'bg-green-100 text-green-800 border border-green-300'
  );

  private loginService = inject(FormLoginService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private usuarioService = inject(UsuarioService);
  logar() {
    this.isLoading.set(true);
    this.statusMessage.set(null);

    this.loginService.logar(this.usuario).subscribe({
      next: (res) => {
        console.log('Login bem-sucedido:', res);

        // ✅ salva sessão
        this.authService.salvarUsuario(res);
        this.usuarioService.salvarUsuario(res);
        // ✅ redireciona para o feed
        this.router.navigate(['/feed']);

        this.isError.set(false);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Erro ao logar:', err);
        this.statusMessage.set('Usuário ou senha incorretos.');
        this.isError.set(true);
        this.isLoading.set(false);
      }
    });
  }
}
