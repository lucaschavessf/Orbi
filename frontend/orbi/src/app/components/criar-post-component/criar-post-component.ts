import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Post } from '../../models/posts';
import { PostService } from '../../services/post-service';
import { UsuarioService } from '../../services/usuario-service'; 
import { Router } from '@angular/router';
import { AzureService } from '../../services/azure-service';

@Component({
  selector: 'app-criar-post-component',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './criar-post-component.html',
  styleUrl: './criar-post-component.css'
})
export class CriarPostComponent {
  selectedTab: 'texto' | 'midia' | 'link' = 'texto';
  selectedCommunity = '';
  communities = ['Tecnologia', 'Programação', 'Design', 'Jogos'];

  statusMessage = signal<string | null>(null);
  isError = signal(false);
  isLoading = signal(false);

  post: Post = { titulo: '', conteudo: '', usernameAutor: '' , urlArquivo: ''};

  private postService = inject(PostService);
  private usuarioService = inject(UsuarioService);
  private azureService = inject(AzureService);
  private router = inject(Router);

  @ViewChild('contentArea', { static: false }) contentArea!: ElementRef<HTMLTextAreaElement>;

  selectedFile: File | null = null;
  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (!file) return;

    if (!(file.type === 'image/png' || file.type === 'image/jpeg' || file.type === 'image/webp')) {
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

  saveDraft() {
    console.log('Rascunho salvo:', this.post);
  }

  postar() {
    this.isLoading.set(true);
    this.statusMessage.set(null);

    const usuario = this.usuarioService.getUsuarioLogado();
    if (usuario) {
      this.post.usernameAutor = usuario.username;
    }
    const containerName = 'imagens';
    const upload$ = this.selectedFile
      ? this.azureService.uploadFile(containerName,this.selectedFile)
      : new Promise<string>((resolve) => resolve(''));

    upload$
      .then((url) => { 
        console.log('URL da imagem:', url);  
        this.post.urlArquivo = url;
        console.log("Enviando para o backend:", this.post);
        this.postService.postar(this.post).subscribe({
          next: (res) => {
            console.log('Post criado com sucesso:', res);
            this.statusMessage.set('Post criado com sucesso!');
            this.isError.set(false);
            this.isLoading.set(false);
            setTimeout(() => this.statusMessage.set(null), 3000);
            this.router.navigate(['/feed']);
          },
          error: (err) => {
            console.error('Erro ao criar post:', err);
            this.statusMessage.set('Erro ao criar post. Verifique o console.');
            this.isError.set(true);
            this.isLoading.set(false);
          }
        });
      })
      .catch((err) => {
        console.error(err);
        this.statusMessage.set('Erro no upload da imagem.');
        this.isError.set(true);
        this.isLoading.set(false);
      });
  }

  private getTextarea(): HTMLTextAreaElement | null {
    return this.contentArea ? this.contentArea.nativeElement : null;
  }

  private setSelectionValue(textarea: HTMLTextAreaElement, value: string) {
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const before = textarea.value.slice(0, start);
    const after = textarea.value.slice(end);
    textarea.value = before + value + after;
    textarea.selectionStart = textarea.selectionEnd = start + value.length;
    this.post.conteudo = textarea.value;
    textarea.focus();
  }

  wrapSelection(prefix: string, suffix?: string) {
    const ta = this.getTextarea();
    if (!ta) return;
    suffix = suffix ?? prefix;
    const start = ta.selectionStart;
    const end = ta.selectionEnd;
    const selected = ta.value.slice(start, end) || 'texto';
    const newText = prefix + selected + suffix;
    this.setSelectionValue(ta, newText);
  }

  insertAtCursor(text: string | null) {
    const ta = this.getTextarea();
    if (!ta || !text) return;
    this.setSelectionValue(ta, text);
  }

  insertLink() {
    const ta = this.getTextarea();
    if (!ta) return;
    const url = prompt('URL (ex: https://...):');
    if (!url) return;
    const start = ta.selectionStart;
    const end = ta.selectionEnd;
    const selected = ta.value.slice(start, end) || url;
    const md = `[${selected}](${url})`;
    this.setSelectionValue(ta, md);
  }

  insertImage() {
    const ta = this.getTextarea();
    if (!ta) return;
    const url = prompt('URL da imagem:');
    if (!url) return;
    const alt = prompt('Texto alternativo (alt):') || '';
    const md = `![${alt}](${url})`;
    this.setSelectionValue(ta, md);
  }

  insertList(type: 'ul' | 'ol') {
    const ta = this.getTextarea();
    if (!ta) return;
    const start = ta.selectionStart;
    const end = ta.selectionEnd;
    const selected = ta.value.slice(start, end);
    let listText = '';
    if (selected.trim()) {
      const lines = selected.split(/\r?\n/).map((l, i) => {
        return type === 'ul' ? `- ${l}` : `${i + 1}. ${l}`;
      });
      listText = lines.join('\n');
    } else {
      listText = type === 'ul' ? '- item 1\n- item 2' : '1. item 1\n2. item 2';
    }
    this.setSelectionValue(ta, listText);
  }
}
