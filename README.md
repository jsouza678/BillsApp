# Bills App

O aplicativo é um assistente pessoal onde o usuário pode colocar suas despesas e receita do mês para acompanhamento.

Ao abrir o aplicativo o usuário pode inserir uma nova despesa ou entrada, verificar as existentes, editá-las, exclui-las e verificar o balanço mensal que dispõe de um gráfico que pode ser atualizado apenas puxando a tela para baixo (Swipe Layout).

A tela de Despesas da aplicação disponibiliza uma lista com todas as despesas existentes com seu valor, descrição, data, se possui anexo ou não, e o status (pago ou não) simbolizado por um switch).

A tela de Receita da aplicação disponibiliza uma lista com todas as entradas existentes com seu valor, descrição, data, se possui anexo ou não, e o status (recebido ou não) simbolizado por um switch).

As duas telas acima podem ser filtradas pelo status (pago ou não / recebido ou não) por meio do menu superior.

Para realizar deslogar da aplicação, basta selecionar os três pontinhos no menu superior e clicar em "Sair".

Para inserir algum item, basta clicar no botão com ícone "+" e preencher os dados. É possível também anexar uma foto de comprovante ao item.

Para editar algum item, basta fazer um toque longo no item, e a tela de edição será aberta.

Para excluir algum item, basta arrastá-lo para a esquerda ou direita.

Ao clicar em um item que possui anexo, ele será aberto na tela em uma pequena janela.

## Algumas screenshots
![expense](https://github.com/jsouza678/BillsApp/blob/master/screenshots/expense.png) 
![income](https://github.com/jsouza678/BillsApp/blob/master/screenshots/income.png)
![edit_insert](https://github.com/jsouza678/BillsApp/blob/master/screenshots/edit_or_insert.png)
![details_other_types](https://github.com/jsouza678/BillsApp/blob/master/screenshots/result.png)

## Ambiente de instalação
* 1: Instale o Android Studio;
* 2: Abra a aplicação;
* 3: Sincronize o projeto;
* 4: Rode o aplicativo em um simulador ou em um device externo.
* 5: Crie uma conta e efetue o login.

## Automação
Ktlint - a task valida se o padrão do código está de acordo com o lint. 
O `./gradlew ktlint` realiza a verificação de todos os componentes do projeto, e retorna o resultado.

KtlintFormat - esta tarefa modifica o código para que ele siga o padrão do lint. 
O `./gradlew ktlintFormat` roda uma rotina que formata o código de acordo com o máximo que o lint pode fazer de modificações para que o código esteja no seu padrão.

 ## Arquitetura
 A aplicação busca o desacoplamento e a escalabilidade em sua arquitetura, fazendo uso do Clean Architecture e do MVVM com Modularização.

 ## Principais dependências

**Coroutines** - _lidando com threads e assincronismo_
 <p>Abordagem sugerida pela Google e com um bom funcionamento com o Live Data, faz bom uso das threads e da Thread Pool do dispositivo, melhorando a performance da aplicação.</p>

 **Material Design** - _layout intuitivo e clean_
 <p>O aplicativo segue os padrões do MaterialDesign para uma melhor experiência do usuário em sua utilização.</p>
 
 **Koin** _injeção de dependência_
 <p>Escolhida por sua simples implementação comparada ao Dagger (E recentemente com o Koin 2.0, o desempenho não é muito diferente).</p>

 **Firebase Storage**
 <p>Com o Cloud Storage, o upload dos arquivos é feito com segurança e facilidade diretamente de dispositivos móveis e navegadores da Web, mesmo em redes intermitentes.</p>

 **Firestore**
 <p> Banco de dados de nuvem NoSQL flexível e escalonável para armazenar e sincronizar dados no desenvolvimento do cliente e do servidor.</p>

 **Firestore UI**
 <p>Escolhida por sua simples implementação comparada ao Dagger (E recentemente com o Koin 2.0, o desempenho não é muito diferente).</p>

 **Firebase Auth**
 <p>Para manter a segurança do aplicativo, foi utilizada a autenticação por meio do Firebase Auth, que foi implementado utilizando a autenticação por e-mail, ou pela Google.</p>

## O que eu gostaria de ter feito

 * _criado testes unitários;_
 
 * _criado testes de ui;_
 
 * _CI_
