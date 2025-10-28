import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

// Importe os 3 componentes que este layout irá unificar
// Caminhos corrigidos para refletir que todos estão em 'components'
import { MenuLateralComponent } from '../menu-lateral-component/menu-lateral-component';
import { FeedComponent } from '../feed-component/feed-component';
import { CabecalhoComponent } from '../cabecalho-component/cabecalho-component'; 

@Component({
  selector: 'app-layout-principal',
  standalone: true,
  // Adicione os componentes importados ao array 'imports'
  imports: [
    CommonModule,
    MenuLateralComponent,
    FeedComponent,
    CabecalhoComponent 
  ],
  templateUrl: './layout-principal-component.html',
  styleUrl: './layout-principal-component.css'
})
export class LayoutPrincipalComponent {
  // Este componente geralmente não precisa de lógica própria,
  // apenas organiza os outros.
}

