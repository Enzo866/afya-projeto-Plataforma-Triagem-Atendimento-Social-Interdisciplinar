# Plataforma de Triagem e Atendimento Social Interdisciplinar

Projeto Integrador desenvolvido com o objetivo de criar uma aplicação para auxiliar na triagem inicial e no direcionamento de usuários aos atendimentos sociais disponíveis.

Nesta primeira etapa, foi desenvolvida uma aplicação Android utilizando Kotlin e Jetpack Compose.

## Objetivo

O objetivo do projeto é facilitar a triagem e o encaminhamento de usuários para áreas de atendimento interdisciplinar, como:

- Psicologia
- Fisioterapia
- Farmácia
- Direito

A aplicação busca organizar o processo inicial de atendimento e apresentar ao usuário a área selecionada para encaminhamento.

## Funcionalidades implementadas

Até o momento, a aplicação possui:

- Campo para informar o nome do usuário
- Campo para informar a idade
- Seleção da área de atendimento
- Opções de Psicologia, Fisioterapia, Farmácia e Direito
- Exibição da especialidade selecionada
- Validação dos campos do formulário
- Exibição do resultado da triagem
- Interface adaptável ao modo claro e escuro do dispositivo
- Rolagem vertical para adaptação a diferentes tamanhos e orientações de tela
- Preservação dos dados durante a rotação do dispositivo

## Tecnologias utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Git
- GitHub

## Conceitos aplicados

Durante o desenvolvimento foram utilizados conceitos abordados na disciplina de Programação para Aplicações e Dispositivos Móveis, incluindo:

- Funções `@Composable`
- `Column`
- `Row`
- `Card`
- `Spacer`
- `Modifier`
- `OutlinedTextField`
- `Button`
- `mutableStateOf`
- `rememberSaveable`
- Recomposição de interface
- State Hoisting
- Componentes Stateful e Stateless

## Estrutura da aplicação

A aplicação foi organizada utilizando separação entre gerenciamento de estado e apresentação da interface.

### TriagemScreen

Responsável pelo gerenciamento dos estados da aplicação, como:

- nome
- idade
- especialidade
- resultado da triagem

### FormularioTriagem

Responsável pela apresentação dos componentes visuais e pelo envio das ações do usuário para o componente responsável pelo estado.

Essa organização utiliza o conceito de State Hoisting e fluxo unidirecional de dados.

## Integrantes

- Eduardo Minuzzi
- Enzo Luigi Bruneto
- Isabela Moreira Carra

## Como executar o projeto

1. Clone o repositório:

```bash
git clone https://github.com/Enzo866/afya-projeto-Plataforma-Triagem-Atendimento-Social-Interdisciplinar/blob/main/app-mobile/app/src/main/java/br/com/afya/triagemsocial/MainActivity.kt
