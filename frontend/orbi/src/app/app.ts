import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
// Importação CRÍTICA para resolver o erro NG0201 (e eliminando o RouterOutlet não usado)
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormCadastroComponent } from './components/form-cadastro-component/form-cadastro-component';

@Component({
  selector: 'app-root',
  standalone: true,
  // HttpClientModule é incluído para fornecer a dependência globalmente
  imports: [CommonModule, HttpClientModule, FormCadastroComponent,RouterModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  title = 'orbi-front';
}
