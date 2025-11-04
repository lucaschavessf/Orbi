import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CabecalhoService } from '../../services/cabecalho-service';

@Component({
  selector: 'app-cabecalho',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cabecalho-component.html',
  styleUrls: ['./cabecalho-component.css']
})
export class CabecalhoComponent {
  searchTerm = '';

  constructor(private cabecalhoService: CabecalhoService) {}

  onSearch() {
    const term = this.searchTerm.trim();
    this.cabecalhoService.setSearchTerm(term);
  }
}
