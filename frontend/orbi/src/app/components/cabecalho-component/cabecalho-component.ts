import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-cabecalho',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cabecalho-component.html',
  styleUrls: ['./cabecalho-component.css']
})
export class CabecalhoComponent {
  @Output() menuToggle = new EventEmitter<void>();
  searchTerm = '';

  onSearch() {
    if (this.searchTerm.trim()) {
      console.log('Pesquisando:', this.searchTerm);
      // redirecionar ou chamar serviço de busca futuramente
    }
  }

  toggleMenu() {
    this.menuToggle.emit();
  }
}
