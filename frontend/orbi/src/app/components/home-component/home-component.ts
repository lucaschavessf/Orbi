import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home-component.html',
  styleUrls: ['./home-component.css']
})
export class HomeComponent {

  constructor(private router: Router) {}

  irParaLogin() {
    this.router.navigate(['/login']);
  }

  irParaCadastro() {
    this.router.navigate(['/cadastrar']);
  }

  scrollParaConteudo() {
  const elemento = document.getElementById('sobre');
  if (elemento) {
    elemento.scrollIntoView({ behavior: 'smooth' });
  }
}

scrollParainfo(){
  const elemento = document.getElementById('conteudo');
  if (elemento) {
    elemento.scrollIntoView({ behavior: 'smooth' });
  }
}


}
