import { Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-criar-post',
  templateUrl: './criar-post-component.html',
  styleUrls: ['./criar-post-component.css']
})
export class CriarPostComponent {
  selectedTab: 'texto' | 'midia' | 'link' = 'texto';
  selectedCommunity = '';
  communities = ['Tecnologia', 'Programação', 'Design', 'Jogos'];

  post = {
    title: '',
    content: '',
    link: '',
    media: null as File | null
  };

  @ViewChild('contentArea', { static: false }) contentArea!: ElementRef<HTMLTextAreaElement>;

  onFileSelected(event: any) {
    const file = event.target.files?.[0] ?? null;
    this.post.media = file;
  }

  saveDraft() {
    console.log('Rascunho salvo:', this.post);
  }

  postar() {
    console.log('Post enviado:', this.post);
  }

  // --- funções para manipular o texto do post ---
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
    this.post.content = textarea.value;
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
