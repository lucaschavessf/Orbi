import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../services/usuario-service';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AzureService } from '../../services/azure-service';

@Component({
  selector: 'app-editar-perfil-component',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './editar-perfil-component.html',
  styleUrl: './editar-perfil-component.css'
})

export class EditarPerfilComponent {

  private usuarioService = inject(UsuarioService);
  private router = inject(Router);
  private authService = inject(AuthService);
  private azureService = inject(AzureService);

  modalAberto = false;
  tituloModal = "";
  descricaoModal = "";
  placeholder = "";
  campoEdicao = "";
  selectedFile: File | null = null;
  uploadingAvatar = false;

  senhaAtual: string = '';
  novaSenha: string = '';
  confirmarSenha: string = '';

  erroSenha: string | null = null;
  isSaving = false;

  campoSendoEditado: 'nomeExibicao' | 'sobre' | 'avatar' | 'username' | 'senha' | null = null;
  usuarioLogado: any;

  constructor() {
    this.usuarioLogado = this.usuarioService.getUsuarioLogado();
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (!file) return;

    if (!(file.type === 'image/png' || file.type === 'image/jpeg' || file.type === 'image/webp')) {
      alert('A imagem deve ser PNG, JPG ou WEBP.');
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      alert('A imagem deve ter no máximo 5MB.');
      return;
    }

    this.selectedFile = file;
  }


  abrirNomeExibicao() {
    this.campoSendoEditado = 'nomeExibicao';
    this.tituloModal = 'Nome de exibição';
    this.descricaoModal = 'Alterar seu nome de exibição não alterará seu nome de usuário.';
    this.placeholder = 'Nome de exibição';
    this.campoEdicao = this.usuarioLogado?.nome ?? '';
    this.resetSenhaFields();
    this.modalAberto = true;
  }

  abrirSobre() {
    this.campoSendoEditado = 'sobre';
    this.tituloModal = 'Sobre';
    this.descricaoModal = 'Edite sua biografia visível no perfil.';
    this.placeholder = 'Sua bio';
    this.campoEdicao = this.usuarioLogado?.bio ?? '';
    this.resetSenhaFields();
    this.modalAberto = true;
  }

  abrirAvatar() {
    this.campoSendoEditado = 'avatar';
    this.tituloModal = 'Foto de perfil';
    this.descricaoModal = 'Envie uma URL válida para seu novo avatar.';
    this.placeholder = 'URL da imagem';
    this.campoEdicao = this.usuarioLogado?.fotoPerfil ?? '';
    this.resetSenhaFields();
    this.modalAberto = true;
  }

  abrirUsername() {
    this.campoSendoEditado = 'username';
    this.tituloModal = 'Nome de usuário';
    this.descricaoModal = 'Seu nome único usado no sistema.';
    this.placeholder = 'Novo username';
    this.campoEdicao = this.usuarioLogado?.username ?? '';
    this.resetSenhaFields();
    this.modalAberto = true;
  }

  abrirSenha() {
    this.campoSendoEditado = 'senha';
    this.tituloModal = 'Senha';
    this.descricaoModal = 'Para alterar a senha, informe a senha atual e a nova senha.';
    this.placeholder = '';
    this.resetSenhaFields();
    this.modalAberto = true;
  }

  fecharModal() {
    this.modalAberto = false;
    this.erroSenha = null;
    this.resetSenhaFields();
  }

  private resetSenhaFields() {
    this.senhaAtual = '';
    this.novaSenha = '';
    this.confirmarSenha = '';
    this.erroSenha = null;
  }

  salvar() {
  const usernameAtual = this.usuarioService.getUsuarioLogado().username;

  const payload: any = {};

  if (this.tituloModal === "Nome de exibição") {
    payload.nome = this.campoEdicao;
  }
  if (this.tituloModal === "Sobre") {
    payload.bio = this.campoEdicao;
  }
  if (this.tituloModal === "Foto de perfil") {
    if (!this.selectedFile) {
      alert("Selecione uma imagem antes de salvar.");
      return;
    }

    this.uploadingAvatar = true;

    const containerName = "imagens-usuarios";

    this.azureService.uploadFile(containerName, this.selectedFile)
      .then((url) => {
        if (!url) throw new Error("Erro ao enviar imagem");

        const payload = { fotoPerfil: url };

        const usernameAtual = this.usuarioService.getUsuarioLogado().username;

        this.usuarioService.atualizarPerfil(usernameAtual, payload).subscribe({
          next: (res: any) => {

            const usuarioAtualizado = res.usuario;
            const novoToken = res.token;

            if (novoToken) this.authService.salvarToken(novoToken);

            this.usuarioService.salvarUsuario(usuarioAtualizado);

            this.selectedFile = null;
            this.uploadingAvatar = false;
            this.modalAberto = false;
            window.location.reload();
          },

          error: (err) => {
            console.error(err);
            alert("Erro ao atualizar avatar.");
            this.uploadingAvatar = false;
          }
        });
      })
      .catch((err) => {
        console.error(err);
        alert("Erro no upload da imagem.");
        this.uploadingAvatar = false;
      });

    return; 
  }

  if (this.tituloModal === "Nome de usuário") {
    payload.username = this.campoEdicao;
  }
  if (this.tituloModal === "Senha") {
    payload.senhaAtual = this.senhaAtual;
    payload.novaSenha = this.novaSenha;
  }

  this.usuarioService.atualizarPerfil(usernameAtual, payload).subscribe({
    next: (res: any) => {

      const usuarioAtualizado = res.usuario;
      const novoToken = res.token;

      if (novoToken) {
        this.authService.salvarToken(novoToken);
        this.usuarioService.salvarUsuario(usuarioAtualizado);
        this.fecharModal();
        window.location.reload();
        return;
      }

      if (payload.username && payload.username !== usernameAtual) {
        console.warn("Username alterado, mas backend não retornou token. Logout automático.");
        this.authService.logout();
        window.location.href = "/login";
        window.location.reload();
        return;
      }

      this.usuarioService.salvarUsuario(usuarioAtualizado);
      this.fecharModal();
      window.location.reload();
    },

    error: (err) => {
      console.error("Erro ao atualizar perfil:", err);
      alert("Erro ao atualizar perfil.");
    }
  });
}

}
