import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../services/post-service';
import { AzureService } from '../../services/azure-service';
import { Post } from '../../models/posts';
import { UsuarioService } from '../../services/usuario-service';

@Component({
  selector: 'app-edit-post-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-post-component.html',
  styleUrl: './edit-post-component.css'
})
export class EditPostComponent {

  statusMessage = signal<string | null>(null);
  isError = signal(false);
  isLoading = signal(false);

  post: Post = {
    titulo: '',
    conteudo: '',
    usernameAutor: '',
    urlArquivo: ''
  };

  private postService = inject(PostService);
  private usuarioService = inject(UsuarioService)
  private azureService = inject(AzureService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  usernameAutor = this.usuarioService.getUsuarioLogado()

  @ViewChild('contentArea', { static: false })
  contentArea!: ElementRef<HTMLTextAreaElement>;

  selectedFile: File | null = null;
  postId: string = '';

  constructor() {
    this.postId = this.route.snapshot.params['id'];

    this.postService.obterPostPorId(this.postId, this.usernameAutor.username).subscribe({
      next: (res) => {
        this.post = res;
      },
      error: () => {
        this.statusMessage.set('Erro ao carregar o post.');
        this.isError.set(true);
      }
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (!file) return;

    if (!(file.type === 'image/png' || file.type === 'image/jpeg')) {
      this.statusMessage.set('A imagem deve ser PNG ou JPG.');
      this.isError.set(true);
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      this.statusMessage.set('A imagem deve ter no máximo 5MB.');
      this.isError.set(true);
      return;
    }

    this.selectedFile = file;
    this.isError.set(false);
  }

  atualizarPost() {
    this.isLoading.set(true);
    this.statusMessage.set(null);

    const upload$ = this.selectedFile
      ? this.azureService.uploadFile('imagens', this.selectedFile)
      : Promise.resolve(this.post.urlArquivo);

    upload$
      .then((url) => {
        const dto = {
          titulo: this.post.titulo,
          conteudo: this.post.conteudo,
          urlArquivo: url,
          usernameAutor: this.usernameAutor.username
        };

        this.postService.atualizarPost(this.postId, dto).subscribe({
          next: () => {
            this.statusMessage.set('Post atualizado com sucesso!');
            this.isError.set(false);
            this.isLoading.set(false);

            setTimeout(() => {
              this.statusMessage.set(null);
              this.router.navigate(['/feed']);
            }, 2000);
          },
          error: () => {
            this.statusMessage.set('Erro ao atualizar o post.');
            this.isError.set(true);
            this.isLoading.set(false);
          }
        });
      })
      .catch(() => {
        this.statusMessage.set('Erro ao enviar imagem.');
        this.isError.set(true);
        this.isLoading.set(false);
      });
  }
  confirmarExclusao() {
    const confirmado = confirm("Tem certeza que deseja excluir este post? Esta ação não pode ser desfeita.");

    if (!confirmado) return;

    this.postService.excluirPost(this.postId).subscribe({
      next: () => {
        alert("Post excluído com sucesso.");
        this.router.navigate(['/feed']);   // volta pra home
      },
      error: (err) => {
        console.error(err);
        alert("Erro ao excluir o post.");
      }
    });
  }


  voltar() {
    this.router.navigate(['/feed']);
  }
}
