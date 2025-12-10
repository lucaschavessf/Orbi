# 📌 Orbi

![Status](https://img.shields.io/badge/status-em_desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?logo=spring)
![Angular](https://img.shields.io/badge/Angular-20-red?logo=angular)
![Licença](https://img.shields.io/badge/licen%C3%A7a-MIT-blue)

## 📖 Descrição

O Orbi é uma aplicação voltada para o ambiente universitário que promove o compartilhamento e a organização de conteúdos acadêmicos, como simulados, slides, mapas mentais, questões, resumos e dicas.
Na plataforma, estudantes e professores podem interagir por meio de posts, comentários e recomendações, criando uma rede colaborativa de aprendizado.

Mais do que um repositório de materiais, o Orbi busca fortalecer a troca de conhecimento e a construção coletiva, tornando o estudo mais dinâmico, acessível e integrado ao cotidiano acadêmico.

## 🚀 Funcionalidades

* Compartilhamento de materiais acadêmicos (simulados, resumos, slides etc.)
* Interação entre alunos e professores por posts, comentários e recomendações
* Organização e categorização de conteúdos
* Rede colaborativa de aprendizado

## ⚙️ Tecnologias Utilizadas

Este projeto é um monorepo que utiliza uma arquitetura full-stack moderna.

### Backend
* **Java 21**
* **Spring Boot 3** (Web, Data JPA, Security, Validation)
* **PostgreSQL** (Banco de dados relacional)
* **Spring Security + BCrypt** (Para criptografia de senhas)
* **Lombok** (Para redução de boilerplate)
* **Maven** (Gerenciador de dependências)

### Frontend
* **Angular 20** (Standalone Components)
* **TypeScript**
* **Tailwind CSS** (Estilização via CDN)
* **RxJS** (Programação reativa)
* **Angular Router** (Roteamento com Auth Guards)

### Arquitetura
O backend segue uma arquitetura em camadas (Controller-Service-Repository) para garantir a separação de responsabilidades e facilitar a manutenção.
* **Controllers:** Responsáveis por expor os endpoints REST, receber requisições e retornar DTOs.
* **Services:** Contêm a lógica de negócio principal da aplicação.
* **Repositories:** Camada de acesso a dados, utilizando Spring Data JPA.
* **DTOs:** (Data Transfer Objects) usados para validar e formatar dados entre o cliente e o servidor.

## 🏁 Como Executar o Projeto

### Pré-requisitos
* Java 21 (ou JDK compatível)
* Node.js (v20 ou superior)
* Um banco de dados PostgreSQL rodando (por padrão, na `localhost:5432` com uma base chamada `orbi`).

### 1. Backend (Spring Boot)
1.  Navegue até a pasta `backend/orbi`.
2.  Configure suas variáveis de banco de dados (usuário e senha) no arquivo `src/main/resources/application.properties`.
3.  Execute a aplicação (pode ser pela sua IDE ou usando o Maven Wrapper):
    ```bash
    # No Linux/macOS
    ./mvnw spring-boot:run
    
    # No Windows
    ./mvnw.cmd spring-boot:run
    ```
4.  A API estará rodando em `http://localhost:8080`.

### 2. Frontend (Angular)
1.  Navegue até a pasta `frontend/orbi`.
2.  Instale as dependências:
    ```bash
    npm install
    ```
3.  Inicie o servidor de desenvolvimento:
    ```bash
    npm start
    ```
4.  Acesse a aplicação em `http://localhost:4200`. O app se conectará automaticamente ao backend rodando na porta 8080.

## 📋 Gestão de Projeto

* [Link do Diagrama de Atividades](https://miro.com/app/board/uXjVJEURmJ4=/)

* [Link do Figma](https://www.figma.com/design/tuF9ldbRI6OFwkwduNUfgB/orbi---prototipo-alta-fidelidade?node-id=158-842)

* [Vídeos de testes](https://drive.google.com/drive/folders/1NNI8i1bGliK2bPYgcJiVzPBa9fy9obgK?usp=sharing)

* [Vídeos do Protótipo Final](https://drive.google.com/drive/u/3/folders/1KDBi9HGBB_fYV21FENrFfNxtsFOCY1gb) 

* [**Link do Board do Jira**](https://projeto-fds-cesar.atlassian.net/jira/software/projects/PFDS/boards/2)

<img width="1904" height="998" alt="image" src="https://github.com/user-attachments/assets/83dc5cd4-7823-4599-8b4e-6532c9790eee" />
  
## 👥 Contribuidores

* [Eulália Regina](https://www.linkedin.com/in/eulalialbuquerque/) - Product Owner
* [Allysson Fellype](https://www.linkedin.com/in/allysson-fellype-868390249/) – Tech Lead
* [Fernando Marinho](https://www.linkedin.com/in/fernando-marinho-05589335a/) - Desenvolvedor Front-end
* [Lucas Chaves](https://www.linkedin.com/in/lucaschavesf/) – Desenvolvedor Back-end
* [Matheus Andrade](https://www.linkedin.com/in/matheus-andrade-silva1/) – Desenvolvedor Back-end


  