# Afya Conecta - Plataforma de Triagem e Atendimento Social Interdisciplinar

Projeto Integrador desenvolvido com o objetivo de criar uma aplicação para auxiliar na triagem inicial, no direcionamento e no agendamento de usuários para atendimentos sociais interdisciplinares.

A aplicação Android está sendo desenvolvida utilizando Kotlin, Jetpack Compose e arquitetura baseada em gerenciamento de estado com ViewModel.

## Objetivo

O objetivo do projeto é facilitar o acesso dos usuários aos atendimentos sociais disponíveis, centralizando em uma única aplicação o processo de triagem, direcionamento e agendamento.

As áreas de atendimento previstas são:

- Psicologia
- Fisioterapia
- Farmácia
- Direito

A aplicação busca tornar o processo de atendimento mais organizado, permitindo que o usuário informe seus dados, escolha a área desejada, realize a triagem e selecione uma data e horário para atendimento.

## Funcionalidades implementadas

Até o momento, a aplicação possui:

- Tela inicial com acesso à triagem
- Formulário de triagem
- Campo para informar o nome
- Campo para informar a idade
- Campo para informar o CPF
- Campo para telefone de contato
- Campo para informar o motivo do atendimento
- Seleção da área de atendimento
- Opções de Psicologia, Fisioterapia, Farmácia e Direito
- Validação dos campos do formulário
- Validação de CPF com 11 dígitos
- Validação de telefone com DDD
- Exibição da área selecionada
- Tela de resultado da triagem
- Seleção de data para atendimento
- Seleção de horário para atendimento
- Bloqueio de agendamentos em horários conflitantes
- Intervalo mínimo de 1 hora entre agendamentos
- Bloqueio de datas e horários anteriores ao momento atual
- Tela para visualização dos atendimentos agendados
- Exibição de especialidade, data, horário e status do atendimento
- Interface adaptável ao tema claro e escuro do dispositivo
- Compatibilidade com as barras do sistema Android
- Rolagem vertical para adaptação a diferentes tamanhos de tela
- Preservação dos estados da aplicação por meio de ViewModel e StateFlow

## Tecnologias utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- ViewModel
- StateFlow
- Navigation Compose
- Git
- GitHub

## Arquitetura e organização

O projeto está organizado utilizando separação entre interface, gerenciamento de estado, navegação e modelos.

Estrutura simplificada:

```text
br.com.afya.triagemsocial
│
├── model
│   ├── Agendamento
│   ├── Especialidade
│   └── TriagemUiState
│
├── navigation
│   └── AppNavigation
│
├── ui
│   ├── agendamento
│   │   └── AgendamentoScreen
│   │
│   ├── atendimentos
│   │   └── MeusAtendimentosScreen
│   │
│   ├── home
│   │   └── HomeScreen
│   │
│   ├── resultado
│   │   └── ResultadoScreen
│   │
│   ├── triagem
│   │   └── TriagemScreen
│   │
│   └── theme
│       ├── Color
│       ├── Theme
│       └── Type
│
├── viewmodel
│   ├── AgendamentoViewModel
│   └── TriagemViewModel
│
└── MainActivity
```

## Triagem

A tela de triagem permite ao usuário informar:

- Nome
- Idade
- CPF
- Telefone para contato
- Motivo do atendimento
- Área de atendimento

Os campos são validados antes que o usuário possa continuar para a próxima etapa.

A área selecionada é armazenada no estado da triagem e utilizada posteriormente no fluxo de agendamento.

## Agendamento

Após a triagem, o usuário pode selecionar:

- Data do atendimento
- Horário do atendimento

A aplicação possui validações para evitar:

- Agendamentos em datas anteriores
- Agendamentos em horários anteriores ao momento atual
- Dois atendimentos dentro do mesmo intervalo de 1 hora

Cada atendimento reserva um intervalo de 1 hora.

Essa validação atualmente é realizada localmente na aplicação.

Em uma próxima etapa, essa regra também será implementada na API para garantir que dois dispositivos diferentes não consigam reservar o mesmo horário simultaneamente.

## Meus Atendimentos

A tela "Meus Atendimentos" apresenta os agendamentos realizados pelo usuário.

Cada atendimento apresenta:

- Especialidade
- Data
- Horário
- Status

## Navegação

A navegação entre as telas é realizada utilizando `Navigation Compose`.

Fluxo atual:

```text
Home
  ↓
Triagem
  ↓
Resultado
  ↓
Agendamento
  ↓
Meus Atendimentos
```

Também existe a opção de acesso à área profissional, que será desenvolvida em uma etapa futura.

## Conceitos aplicados

Durante o desenvolvimento foram utilizados conceitos abordados na disciplina de Programação para Aplicações e Dispositivos Móveis, incluindo:

- Funções `@Composable`
- Jetpack Compose
- `Column`
- `Row`
- `Card`
- `Spacer`
- `Modifier`
- `Surface`
- `OutlinedTextField`
- `Button`
- `OutlinedButton`
- Material Theme
- State Hoisting
- Recomposição de interface
- ViewModel
- StateFlow
- Fluxo unidirecional de dados
- Navigation Compose
- Validação de formulários
- DatePicker
- TimePicker

## Próximas etapas

As próximas etapas previstas para o projeto são:

- Desenvolvimento da API REST
- Integração do aplicativo com a API
- Persistência dos dados em banco de dados
- Armazenamento dos usuários e agendamentos
- Verificação de conflitos de horários no servidor
- Consulta de agendamentos por CPF
- Cancelamento e reagendamento
- Confirmação de presença
- Área de acesso profissional
- Listagem de solicitações por especialidade
- Filtros por nome, CPF e status
- Alteração do status do atendimento
- Possível implementação de lembretes de atendimento

## API planejada

A API será responsável por centralizar os dados da aplicação e garantir a consistência dos agendamentos.

Estrutura planejada:

```text
Aplicativo Android
        ↓
      Retrofit
        ↓
     API REST
        ↓
   Banco de dados
```

Entre as operações previstas estão:

```http
POST /triagens

POST /agendamentos

GET /agendamentos

GET /agendamentos/:cpf

PUT /agendamentos/:id

DELETE /agendamentos/:id
```

A API também deverá impedir a criação de agendamentos que entrem em conflito com horários já cadastrados.

## Integrantes

- Eduardo Minuzzi
- Enzo Luigi Bruneto
- Isabela Moreira Carra

## Como executar o projeto

1. Clone o repositório:

```bash
git clone https://github.com/Enzo866/afya-projeto-Plataforma-Triagem-Atendimento-Social-Interdisciplinar
```

2. Abra o projeto no Android Studio.

3. Aguarde a sincronização do Gradle.

4. Execute o projeto em um dispositivo Android ou emulador.

## Status do projeto

Projeto em desenvolvimento.

Atualmente estão implementados o fluxo de triagem, direcionamento, agendamento local, validação de horários e listagem de atendimentos.

A próxima etapa principal será o desenvolvimento da API e a persistência dos dados.
