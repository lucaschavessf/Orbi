import { Component } from '@angular/core';
import { NgFor, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatAssistentService } from '../../services/chat-assistent-service';

@Component({
  selector: 'app-chat-assistent-component',
  standalone: true,
  imports: [NgFor, NgClass, FormsModule],
  templateUrl: './chat-assistent-component.html',
  styleUrl: './chat-assistent-component.css'
})
export class ChatAssistentComponent {
  messages: { role: 'user' | 'ai', text: string }[] = [];
  input = '';
  loading = false;

  constructor(private chatService: ChatAssistentService) {}

  sendMessage() {
    const text = this.input.trim();
    if (!text || this.loading) return;
    this.messages.push({ role: 'user', text });
    this.input = '';
    this.loading = true;

    this.chatService.sendMessage(text).subscribe({
      next: (response) => {
        this.messages.push({ role: 'ai', text: response });
        this.loading = false;
      },
      error: () => {
        this.messages.push({ role: 'ai', text: 'Ocorreu um erro ao se comunicar com o assistente.' });
        this.loading = false;
      }
    });
  }
}
