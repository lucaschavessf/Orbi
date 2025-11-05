import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { MenuLateralComponent } from '../menu-lateral-component/menu-lateral-component';
import { CabecalhoComponent } from '../cabecalho-component/cabecalho-component';

@Component({
  selector: 'app-layout-principal',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule, 
    MenuLateralComponent,
    CabecalhoComponent
  ],
  templateUrl: './layout-principal-component.html',
  styleUrl: './layout-principal-component.css'
})
export class LayoutPrincipalComponent {}
