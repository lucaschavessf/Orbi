import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { CabecalhoService } from '../../services/cabecalho-service';
import { AuthService } from '../../services/auth-service';
import { Usuario } from '../../models/usuario'; 
import { UsuarioService } from '../../services/usuario-service';
import { AzureService } from '../../services/azure-service';

@Component({
  selector: 'app-menu-lateral',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './menu-lateral-component.html',
  styleUrls: ['./menu-lateral-component.css']
})
export class MenuLateralComponent implements OnInit {
  
  selectedImage: string | null = null;
  usuario: Usuario | null = null;
  url_perfil: string = '';
  esconderPerfilMenu = false;
  private usuarioService = inject(UsuarioService);
  private azureService = inject(AzureService);
  constructor(private router: Router, private cabecalhoService: CabecalhoService,private route: ActivatedRoute) {}

  ngOnInit() {
    this.carregarUsuario();
    const usuarioLogado = this.usuarioService.getUsuarioLogado();

    this.router.events.subscribe(() => {
      const urlAtual = this.router.url; 

      if (urlAtual.startsWith('/perfil/')) {
        const usernameDaUrl = urlAtual.split('/')[2]; 

        this.esconderPerfilMenu =
          usernameDaUrl === usuarioLogado?.username;
      } else {
        this.esconderPerfilMenu = false;
      }
    });
  }

  carregarUsuario() {
    const usuarioLogado = this.usuarioService.getUsuarioLogado();
    if (!usuarioLogado) {
      this.usuario = null;
      return;
    }

    this.usuario = usuarioLogado;
    this.url_perfil = `/perfil/${usuarioLogado.username}`;
    if (usuarioLogado.fotoPerfil) {
      const fileName = usuarioLogado.fotoPerfil.split('/').pop();
      const containerName = 'imagens-usuarios';
      this.azureService.generateReadUrl(containerName,fileName).subscribe({
        next: (resp: any) => {
          this.selectedImage = resp?.uploadUrl ?? resp?.url ?? null;
        },
        error: (err: any) => {
          console.error('Erro ao gerar URL da imagem:', err);
        }
      });
    }
  }
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const reader = new FileReader();
      reader.onload = () => this.selectedImage = reader.result as string;
      reader.readAsDataURL(input.files[0]);
    }
  }

  onPaginaInicialClick() {
    this.cabecalhoService.clearSearchTerm?.(); 
    if (this.router.url === '/feed') {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/feed']);
      });
    } else {
      this.router.navigate(['/feed']);
    }
  }

  onPerfilClick() {
    this.cabecalhoService.clearSearchTerm?.(); 
    if (this.router.url === this.url_perfil) {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate([this.url_perfil]);
      });
    } else {
      this.router.navigate([this.url_perfil]);
    }
  }

  onFavoritosClick() {
    this.cabecalhoService.clearSearchTerm?.();
    if (this.router.url === '/favoritos') {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/favoritos']);
      });
    } else {
      this.router.navigate(['/favoritos']);
    }
  }

  onAssistentClick() {
    if (this.router.url === '/chat_ai') {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/chat_ai']);
      });
    } else {
      this.router.navigate(['/chat_ai']);
    }
  }
}
