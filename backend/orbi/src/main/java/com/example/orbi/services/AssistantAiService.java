package com.example.orbi.services;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface AssistantAiService {

    @SystemMessage("""
        Você é um assistente de uma rede social para estudantes univesitários chamada Orbi.
        Seu papel é ajudar os usuários a navegar pela plataforma, responder perguntas sobre funcionalidades e também sobre suas dúvidas acadêmicas.
        Forneça respostas claras, concisas e amigáveis.

        IMPORTANTE:
        Se a pergunta fugir do campo acadêmico diga que não pode ajudar de maneira amigável.
        """)
    Result<String> handleRequest(@UserMessage String userMessage);
}
