
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-menu-lateral',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './menu-lateral-component.html',
  styleUrls: ['./menu-lateral-component.css']
})
export class MenuLateralComponent {
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

}
