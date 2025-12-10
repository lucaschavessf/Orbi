import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormCadastroService } from '../../services/form-cadastro-service';
import { Usuario } from '../../models/usuario';
import { AzureService } from '../../services/azure-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-form-cadastro-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-cadastro-component.html',
  styleUrls: ['./form-cadastro-component.css']
})
export class FormCadastroComponent {

  usuario: Usuario = {
    username: '',
    nome: '',
    email: '',
    senha: '',
    tipo: 'ALUNO',
    curso: '',
    bio: '',
    fotoPerfil: ''
  };

  selectedFile: File | null = null;

  statusMessage = signal<string | null>(null);
  isError = signal(false);
  isLoading = signal(false);

  statusClass = computed(() =>
    this.isError()
      ? 'bg-red-100 text-red-800 border border-red-300'
      : 'bg-green-100 text-green-800 border border-green-300'
  );

  private cadastroService = inject(FormCadastroService);
  private azureService = inject(AzureService);
  private router = inject(Router);

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (!file) return;

    if (!(file.type === 'image/png' || file.type === 'image/jpeg' || file.type === 'image/webp')) {
      this.statusMessage.set('A imagem deve ser PNG ou JPG.');
      this.isError.set(true);
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      this.statusMessage.set('A imagem deve ter no máximo 5MB.');
      this.isError.set(true);
      return;
    }

    this.selectedFile = file;
    this.isError.set(false);
  }

  cadastrar() {
    this.isLoading.set(true);
    this.statusMessage.set(null);
    const containerName = 'imagens-usuarios';
    const upload$ = this.selectedFile
      ? this.azureService.uploadFile(containerName,this.selectedFile)
      : new Promise<string>((resolve) => resolve(''));

    upload$
      .then((url) => {
        if (url) this.usuario.fotoPerfil = url;

        this.cadastroService.cadastrar(this.usuario).subscribe({
          next: (res) => {
            this.statusMessage.set('Usuário cadastrado com sucesso!');
            this.isError.set(false);
            this.isLoading.set(false);

            this.usuario = {
              username: '',
              nome: '',
              email: '',
              senha: '',
              tipo: 'ALUNO',
              curso: '',
              bio: '',
              fotoPerfil: ''
            };

            this.selectedFile = null;

            setTimeout(() => this.statusMessage.set(null), 3000);
            this.router.navigate(['/login']);
          },
          error: (err) => {
            console.error(err);
            this.statusMessage.set('Erro ao cadastrar usuário.');
            this.isError.set(true);
            this.isLoading.set(false);
          }
        });
      })
      .catch((err) => {
        console.error(err);
        this.statusMessage.set('Erro no upload da imagem.');
        this.isError.set(true);
        this.isLoading.set(false);
      });
  }
}
