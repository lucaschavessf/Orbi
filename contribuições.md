<!-- omit in toc -->
# Contribuindo para o Orbi

Antes de tudo, obrigado por dedicar seu tempo para contribuir! ❤️

Todos os tipos de contribuições são encorajados e valorizados. Veja a [Tabela de Conteúdos](#tabela-de-conteúdos) para diferentes formas de ajudar e detalhes sobre como este projeto as gerencia. Por favor, certifique-se de ler a seção relevante antes de fazer sua contribuição. Isso facilitará muito para nós, mantenedores, e tornará a experiência mais tranquila para todos os envolvidos. A comunidade aguarda ansiosamente suas contribuições. 🎉

> E se você gosta do projeto, mas simplesmente não tem tempo para contribuir, tudo bem. Existem outras formas fáceis de apoiar o projeto e mostrar sua apreciação, o que também nos deixaria muito felizes:
> - Dê uma estrela ao projeto
> - Tuíte sobre ele
> - Referencie este projeto no readme do seu projeto
> - Mencione o projeto em meetups locais e conte para seus amigos/colegas

<!-- omit in toc -->
## Tabela de Conteúdos

- [Tenho uma Pergunta](#tenho-uma-pergunta)
  - [Quero Contribuir](#quero-contribuir)
  - [Reportando Bugs](#reportando-bugs)
  - [Sugerindo Melhorias](#sugerindo-melhorias)
  - [Sua Primeira Contribuição de Código](#sua-primeira-contribuição-de-código)
  - [Melhorando a Documentação](#melhorando-a-documentação)
- [Guias de Estilo](#guias-de-estilo)
  - [Mensagens de Commit](#mensagens-de-commit)
- [Junte-se à Equipe do Projeto](#junte-se-à-equipe-do-projeto)



## Tenho uma Pergunta

> Se você deseja fazer uma pergunta, presumimos que você leu a [Documentação](https://docs.google.com/document/d/1e1IR2uWIc5xiaBD9DcYg5kXQmlyXAlU2cJ0Jwj3AkOw/edit?tab=t.0) disponível.

Antes de fazer uma pergunta, é melhor procurar por [Issues](https://github.com/lucaschavessf/Orbi/issues) existentes que possam ajudá-lo. Caso você tenha encontrado uma issue adequada e ainda precise de esclarecimentos, você pode escrever sua pergunta nesta issue. Também é aconselhável pesquisar na internet por respostas primeiro.

Se mesmo assim você ainda sentir a necessidade de fazer uma pergunta e precisar de esclarecimentos, recomendamos o seguinte:

- Abra uma [Issue](https://github.com/lucaschavessf/Orbi/issues/new).
- Forneça o máximo de contexto possível sobre o problema que você está enfrentando.
- Forneça as versões do projeto e da plataforma, dependendo do que parecer relevante.

Vamos então cuidar da issue o mais rápido possível.

<!--
You might want to create a separate issue tag for questions and include it in this description. People should then tag their issues accordingly.

Depending on how large the project is, you may want to outsource the questioning, e.g. to Stack Overflow or Gitter. You may add additional contact and information possibilities:
- IRC
- Slack
- Gitter
- Stack Overflow tag
- Blog
- FAQ
- Roadmap
- E-Mail List
- Forum
-->

## Quero Contribuir

> ### Aviso Legal <!-- omit in toc -->
> Ao contribuir para este projeto, você deve concordar que você é o autor de 100% do conteúdo, que você possui os direitos necessários sobre o conteúdo e que o conteúdo que você contribuir pode ser fornecido sob a licença do projeto.

### Reportando Bugs

<!-- omit in toc -->
#### Antes de Enviar um Relatório de Bug

Um bom relatório de bug não deve deixar outras pessoas precisando correr atrás de você para obter mais informações. Portanto, pedimos que você investigue cuidadosamente, colete informações e descreva o problema em detalhes em seu relatório. Por favor, complete as etapas a seguir com antecedência para nos ajudar a corrigir qualquer bug potencial o mais rápido possível.

- Certifique-se de que você está usando a versão mais recente.
- Determine se seu bug é realmente um bug e não um erro do seu lado, por exemplo, usando componentes/versões de ambiente incompatíveis (Certifique-se de que você leu a [documentação](https://docs.google.com/document/d/1e1IR2uWIc5xiaBD9DcYg5kXQmlyXAlU2cJ0Jwj3AkOw/edit?tab=t.0). Se você está procurando suporte, você pode querer verificar [esta seção](#tenho-uma-pergunta)).
- Para ver se outros usuários experimentaram (e potencialmente já resolveram) o mesmo problema que você está tendo, verifique se já não existe um relatório de bug para seu bug ou erro no [rastreador de bugs](https://github.com/lucaschavessf/Orbi/issues?q=label%3Abug).
- Certifique-se também de pesquisar na internet (incluindo Stack Overflow) para ver se usuários fora da comunidade do GitHub discutiram o problema.
- Colete informações sobre o bug:
  - Stack trace (Traceback)
  - SO, Plataforma e Versão (Windows, Linux, macOS, x86, ARM)
  - Versão do interpretador, compilador, SDK, ambiente de execução, gerenciador de pacotes, dependendo do que parecer relevante.
  - Possivelmente sua entrada e a saída
  - Você pode reproduzir o problema de forma confiável? E você também pode reproduzi-lo com versões mais antigas?

<!-- omit in toc -->
#### Como Enviar um Bom Relatório de Bug?

> Você nunca deve reportar problemas relacionados à segurança, vulnerabilidades ou bugs incluindo informações sensíveis no rastreador de issues, ou em qualquer outro lugar público. Em vez disso, bugs sensíveis devem ser enviados por e-mail para noreply.orbi@gmail.com.
<!-- You may add a PGP key to allow the messages to be sent encrypted as well. -->

Usamos issues do GitHub para rastrear bugs e erros. Se você se deparar com um problema no projeto:

- Abra uma [Issue](https://github.com/lucaschavessf/Orbi/issues/new). (Como não podemos ter certeza neste momento se é um bug ou não, pedimos que você não fale sobre um bug ainda e não rotule a issue.)
- Explique o comportamento que você esperaria e o comportamento real.
- Por favor, forneça o máximo de contexto possível e descreva os *passos de reprodução* que outra pessoa pode seguir para recriar o problema por conta própria. Isso geralmente inclui seu código. Para bons relatórios de bug, você deve isolar o problema e criar um caso de teste reduzido.
- Forneça as informações que você coletou na seção anterior.

Uma vez arquivada:

- A equipe do projeto rotulará a issue adequadamente.
- Um membro da equipe tentará reproduzir o problema com os passos que você forneceu. Se não houver passos de reprodução ou nenhuma maneira óbvia de reproduzir o problema, a equipe pedirá esses passos e marcará a issue como `needs-repro`. Bugs com a tag `needs-repro` não serão tratados até que sejam reproduzidos.
- Se a equipe conseguir reproduzir o problema, ele será marcado como `needs-fix`, bem como possivelmente outras tags (como `critical`), e a issue será deixada para ser [implementada por alguém](#sua-primeira-contribuição-de-código).

<!-- You might want to create an issue template for bugs and errors that can be used as a guide and that defines the structure of the information to be included. If you do so, reference it here in the description. -->


### Sugerindo Melhorias

Esta seção o orienta através do envio de uma sugestão de melhoria para o Orbi, **incluindo recursos completamente novos e pequenas melhorias na funcionalidade existente**. Seguir essas diretrizes ajudará os mantenedores e a comunidade a entender sua sugestão e encontrar sugestões relacionadas.

<!-- omit in toc -->
#### Antes de Enviar uma Melhoria

- Certifique-se de que você está usando a versão mais recente.
- Leia a [documentação](https://docs.google.com/document/d/1e1IR2uWIc5xiaBD9DcYg5kXQmlyXAlU2cJ0Jwj3AkOw/edit?tab=t.0) cuidadosamente e descubra se a funcionalidade já está coberta, talvez por uma configuração individual.
- Realize uma [busca](https://github.com/lucaschavessf/Orbi/issues) para ver se a melhoria já foi sugerida. Se foi, adicione um comentário à issue existente em vez de abrir uma nova.
- Descubra se sua ideia se encaixa no escopo e objetivos do projeto. Cabe a você apresentar um caso convincente para convencer os desenvolvedores do projeto dos méritos deste recurso. Tenha em mente que queremos recursos que sejam úteis para a maioria de nossos usuários e não apenas para um pequeno subconjunto. Se você está apenas mirando uma minoria de usuários, considere escrever uma biblioteca de complemento/plugin.

<!-- omit in toc -->
#### Como Enviar uma Boa Sugestão de Melhoria?

Sugestões de melhorias são rastreadas como [issues do GitHub](https://github.com/lucaschavessf/Orbi/issues).

- Use um **título claro e descritivo** para a issue para identificar a sugestão.
- Forneça uma **descrição passo a passo da melhoria sugerida** com o máximo de detalhes possível.
- **Descreva o comportamento atual** e **explique qual comportamento você esperava ver em vez disso** e por quê. Neste ponto, você também pode dizer quais alternativas não funcionam para você.
- Você pode querer **incluir capturas de tela ou gravações de tela** que ajudem você a demonstrar os passos ou apontar a parte à qual a sugestão está relacionada. Você pode usar [LICEcap](https://www.cockos.com/licecap/) para gravar GIFs no macOS e Windows, e o [gravador de tela integrado no GNOME](https://help.gnome.org/users/gnome-help/stable/screen-shot-record.html.en) ou [SimpleScreenRecorder](https://github.com/MaartenBaert/ssr) no Linux. <!-- this should only be included if the project has a GUI -->
- **Explique por que esta melhoria seria útil** para a maioria dos usuários do Orbi. Você também pode querer apontar outros projetos que resolveram isso melhor e que poderiam servir de inspiração.
