import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormCadastroService } from '../../services/form-cadastro-service';
import { Usuario } from '../../models/usuario';

@Component({
  selector: 'app-form-cadastro-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-cadastro-component.html',
  styleUrls: ['./form-cadastro-component.css']
})
export class FormCadastroComponent {
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
  private cadastroService = inject(FormCadastroService);

  cadastrar() {
    this.isLoading.set(true);
    this.statusMessage.set(null);

    this.cadastroService.cadastrar(this.usuario).subscribe({
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
