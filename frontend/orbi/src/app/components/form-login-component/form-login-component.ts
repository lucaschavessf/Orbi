import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../../models/usuario';
import { FormLoginService } from '../../services/form-login-service';

@Component({
  selector: 'app-form-login-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-login-component.html',
  styleUrl: './form-login-component.css'
})
export class FormLoginComponent {
  usuario: Usuario = { username: '',nome: '', cpf: '',email: '', senha: '' ,tipo: 'ALUNO', curso: '', bio: ''};
  
  // Signals para feedback de UI
  statusMessage = signal<string | null>(null);
  isError = signal(false);
  isLoading = signal(false);

  // Classe CSS dinâmica baseada no estado de erro
  statusClass = computed(() =>
    this.isError()
      ? 'bg-red-100 text-red-800 border border-red-300'
      : 'bg-green-100 text-green-800 border border-green-300'
  );

  // Injeção de dependência via 'inject'
  private loginService = inject(FormLoginService);

  logar() {
    this.isLoading.set(true);
    this.statusMessage.set(null);

    this.loginService.logar(this.usuario).subscribe({
      next: (res) => {
        console.log('Usuário cadastrado com sucesso:', res);
        this.statusMessage.set('Usuário cadastrado com sucesso!');
        this.isError.set(false);
        this.usuario = { username: '',nome: '', cpf: '',email: '', senha: '' ,tipo: 'ALUNO', curso: '', bio: ''};
        this.isLoading.set(false);
        setTimeout(() => this.statusMessage.set(null), 3000); // Oculta após 3s
      },
      error: (err) => {
        console.error('Erro ao cadastrar:', err);
        this.statusMessage.set('Erro ao cadastrar usuário. Verifique o console.');
        this.isError.set(true);
        this.isLoading.set(false);
      }
    });
  }

}


