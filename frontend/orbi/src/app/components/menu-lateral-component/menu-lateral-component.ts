import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { CabecalhoService } from '../../services/cabecalho-service';

@Component({
  selector: 'app-menu-lateral',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './menu-lateral-component.html',
  styleUrls: ['./menu-lateral-component.css']
})
export class MenuLateralComponent {
  selectedImage: string | ArrayBuffer | null = null;

  recentes = [
    { label: 'r/datasciencebr', route: '/r/datasciencebr' },
    { label: 'r/DistantHorizons', route: '/r/distanthorizons' }
  ];

  categorias = [
    'Cultura da internet',
    'Jogos',
    'Tecnologia',
    'Filmes e TV',
    'Perguntas'
  ];

  constructor(private router: Router, private cabecalhoService: CabecalhoService) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const reader = new FileReader();
      reader.onload = () => this.selectedImage = reader.result;
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

  onFavoritosClick() {
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
