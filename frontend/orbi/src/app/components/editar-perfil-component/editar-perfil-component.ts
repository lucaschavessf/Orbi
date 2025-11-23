import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // <-- necessário para ngModel

@Component({
  selector: 'app-editar-perfil-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './editar-perfil-component.html',
  styleUrl: './editar-perfil-component.css'
})
export class EditarPerfilComponent {
 modalAberto = false;

tituloModal = "";
descricaoModal = "";
placeholder = "";
campoEdicao = "";

// abre modal de Nome de Exibição
abrirNomeExibicao() {
  this.tituloModal = "Nome de exibição";
  this.descricaoModal = "Alterar seu nome de exibição não alterará seu nome de usuário";
  this.placeholder = "Nome de exibição";

  this.campoEdicao = ""; 
  this.modalAberto = true;
}

// abre modal de Sobre
abrirSobre() {
  this.tituloModal = "Sobre";
  this.descricaoModal = "Edite sua biografia visível no perfil.";
  this.placeholder = "Sua bio";
  this.campoEdicao = "";
  this.modalAberto = true;
}

// abre modal de Avatar
abrirAvatar() {
  this.tituloModal = "Avatar";
  this.descricaoModal = "Envie uma nova imagem de avatar.";
  this.placeholder = "URL da imagem (ou outra ação)";
  this.campoEdicao = "";
  this.modalAberto = true;
}

fecharModal() {
  this.modalAberto = false;
}

salvar() {
  console.log("Salvar valor:", this.campoEdicao);

  // aqui você pode chamar seu backend para salvar

  this.modalAberto = false;
}
}
