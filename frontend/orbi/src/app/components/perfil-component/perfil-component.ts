import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsuarioService } from '../../services/usuario-service';
import { AzureService } from '../../services/azure-service';
import { CommonModule } from '@angular/common';
import { FeedPerfilComponent } from "../feed-perfil-component/feed-perfil-component";

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FeedPerfilComponent],
  templateUrl: './perfil-component.html',
  styleUrls: ['./perfil-component.css']
})
export class PerfilComponent implements OnInit {

  usuario: any = null;
  fotoUrl: string | null = null;

  private route = inject(ActivatedRoute);
  private usuarioService = inject(UsuarioService);
  private azureService = inject(AzureService);

  ngOnInit(): void {
    const usernameUrl = this.route.snapshot.paramMap.get('username');
    const usuarioLogado = this.usuarioService.getUsuarioLogado();

    if (usuarioLogado && usernameUrl === usuarioLogado.username) {
      this.usuario = usuarioLogado;
      this.carregarFoto(usuarioLogado);
      return;
    }

    if (usernameUrl) {
      this.usuarioService.getUsuario(usernameUrl).subscribe({
        next: (resp) => {
          this.usuario = resp;
          this.carregarFoto(resp);
        },
        error: () => {
          this.usuario = null;
        }
      });
    }
  }

  carregarFoto(usuario: any) {
    if (!usuario.fotoPerfil) return;

    const fileName = usuario.fotoPerfil.split('/').pop();
    const container = 'imagens-usuarios';

    this.azureService.generateReadUrl(container, fileName!).subscribe({
      next: (resp: any) => {
        this.fotoUrl = resp?.uploadUrl ?? resp?.url ?? null;
      }
    });
  }
}
