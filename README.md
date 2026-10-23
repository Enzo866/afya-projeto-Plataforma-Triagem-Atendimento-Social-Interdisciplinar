# Afya Conecta

Plataforma de Triagem e Atendimento Social Interdisciplinar

Aplicação Android desenvolvida em Kotlin e Jetpack Compose para facilitar o acesso aos atendimentos sociais gratuitos de Psicologia, Fisioterapia, Farmácia e Direito. O projeto reúne o registro da necessidade, a escolha da área de atendimento, o agendamento e a consulta dos registros no aparelho.

## Integrantes

- Eduardo Minuzzi
- Enzo Luigi Bruneto
- Mandrik Rufatto de Paula

## Funcionalidades

### Identificação e solicitação

- Formulário com nome, idade, CPF, telefone e motivo do atendimento.
- Escolha entre Psicologia, Fisioterapia, Farmácia e Direito.
- Validação de preenchimento e formato antes de prosseguir.
- Apresentação da especialidade escolhida antes do agendamento.

O CPF é verificado pelo formato de 11 dígitos; não há validação dos dígitos verificadores. O telefone é verificado pelo tamanho de 10 ou 11 dígitos, incluindo DDD.

### Agendamento e acompanhamento

- Informação da data no formato `dd/MM/aaaa` e do horário no formato `HH:mm`.
- Validação de data e horário futuros antes da gravação.
- Persistência local dos agendamentos com Room.
- Listagem com especialidade, data, horário e status.
- Atualização da listagem por observação do banco com `Flow`.
- Edição de data e horário, preservando o identificador e os demais dados.
- Exclusão com diálogo de confirmação.
- Confirmação antes de descartar alterações na edição do horário.

Nesta versão, data e horário são informados em campos de texto Compose. A implementação anterior com seletores nativos de calendário e horário foi substituída na entrega de persistência.

## Tecnologias

- Kotlin
- Jetpack Compose
- Material Design 3
- AndroidX ViewModel
- StateFlow e Flow
- Kotlin Coroutines
- Navigation Compose
- Room Database
- Kotlin Symbol Processing (KSP)
- Gradle
- Git e GitHub

Retrofit e DataStore pertencem à frente de integração seguinte e não são apresentados como recursos concluídos nesta versão.

## Arquitetura

O acesso aos dados segue o fluxo:

```text
Interface Compose
        |
        v
     ViewModel
        |
        v
    Repository
        |
        v
Local Data Source
        |
        v
    DAO / Room
```

- **Interface:** apresenta o estado e encaminha as ações do usuário.
- **ViewModel:** expõe `StateFlow` imutável e executa as operações assíncronas em `viewModelScope`.
- **Repository:** valida os dados de entrada, converte os modelos e coordena o acesso à persistência.
- **Local Data Source:** acessa o DAO e agrupa a verificação de conflito e a gravação em transação.
- **Room:** mantém os registros no banco SQLite local.

Os ViewModels não recebem `Context`. O contexto da aplicação é utilizado na montagem da instância do banco.

### Organização dos arquivos

Os fontes ficam em `app-mobile/app/src/main/java/br/com/afya/triagemsocial`.

```text
br.com.afya.triagemsocial/
|-- data/
|   |-- local/
|   |   |-- AgendamentoEntity.kt
|   |   |-- Conversoes.kt
|   |   |-- AgendamentoDao.kt
|   |   |-- AppDatabase.kt
|   |   `-- AgendamentoLocalDataSource.kt
|   `-- repository/
|       `-- AgendamentoRepository.kt
|-- model/
|   |-- Agendamento.kt
|   |-- Especialidade.kt
|   |-- StatusAgendamento.kt
|   `-- TriagemUiState.kt
|-- navigation/
|   `-- AppNavigation.kt
|-- ui/
|   |-- agendamento/
|   |   |-- AgendamentoScreen.kt
|   |   `-- EditarAgendamentoScreen.kt
|   |-- atendimentos/
|   |   `-- MeusAtendimentosScreen.kt
|   |-- home/
|   |-- resultado/
|   |-- triagem/
|   `-- theme/
|-- viewmodel/
|   |-- AgendamentoViewModel.kt
|   `-- TriagemViewModel.kt
`-- MainActivity.kt
```

## Persistência local

O banco `afya_conecta.db` armazena nome, CPF, idade, telefone, motivo, especialidade, início e status do atendimento, além de um identificador gerado pelo banco.

O início é armazenado em milissegundos e convertido para data e horário na apresentação. O tratamento de data e hora utiliza o fuso do aparelho.

O DAO disponibiliza inserção, leitura reativa da lista, busca por identificador, atualização e exclusão. As operações de gravação são assíncronas. A navegação após o cadastro ocorre somente depois da confirmação da gravação.

A persistência foi projetada para manter os registros após encerrar e reabrir o aplicativo. Esse comportamento integra o checklist de verificação abaixo.

## Regras de agendamento

A implementação local adota as seguintes regras, que devem ser confirmadas com os responsáveis pelos atendimentos:

- Cada atendimento ocupa uma hora na mesma especialidade.
- Intervalos menores que uma hora entre os inícios de dois atendimentos da mesma especialidade geram conflito.
- Uma diferença de exatamente uma hora é permitida.
- Especialidades diferentes não bloqueiam mutuamente seus horários.
- Registros com status `CANCELADO` não bloqueiam uma vaga.
- Na edição, o próprio registro é desconsiderado na busca de conflitos.
- A consulta de conflitos e a gravação são executadas na mesma transação local.

A existência do status `CANCELADO` não significa que já exista uma funcionalidade de cancelamento. Essa operação continua pendente nesta versão.

## Navegação

```text
Home
|-- Triagem -> Resultado -> Agendamento -> Meus Atendimentos
`-- Meus Atendimentos
    |-- Editar horário -> Salvar ou descartar alterações
    `-- Excluir -> Confirmar ou cancelar a exclusão
```

O tema acompanha a configuração clara ou escura do dispositivo. A área profissional ainda não está implementada; o botão existente na versão base não executa uma operação de acesso.

## Como executar

### Pré-requisitos

- Android Studio compatível com a configuração Gradle do projeto.
- Android SDK correspondente ao `compileSdk` declarado no módulo `app`.
- JDK compatível com o Gradle e o Android Gradle Plugin utilizados.
- Internet na primeira sincronização para baixar as dependências.
- Emulador ou dispositivo com Android 7.0 (API 24) ou superior.

As versões das bibliotecas e dos plugins estão declaradas nos arquivos Gradle e em `app-mobile/gradle/libs.versions.toml`. O SDK de compilação é 37 na base utilizada para esta entrega.

### Execução pelo Android Studio

1. Clone o repositório:

   ```bash
   git clone https://github.com/Enzo866/afya-projeto-Plataforma-Triagem-Atendimento-Social-Interdisciplinar.git
   ```

2. Abra a pasta **`app-mobile`** no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Configure o SDK e o JDK, caso o Android Studio solicite.
5. Selecione um emulador ou dispositivo Android.
6. Execute o módulo `app`.

## Limitações e próximas etapas

### Integração técnica

- Confirmar a compilação e o funcionamento da camada Room no projeto integrado.
- Integrar Retrofit e uma fonte REST, incluindo carregamento, sucesso, erro e nova tentativa.
- Persistir uma preferência com DataStore ou SharedPreferences.
- Completar o menu principal com os grupos funcionais previstos na tarefa.
- Ampliar os testes das regras e verificar diferentes dimensões de tela.

### Evolução do produto

- Definir o questionário e as regras de direcionamento por triagem.
- Consultar a disponibilidade institucional por área.
- Implementar identificação e controle de acesso aos registros.
- Sincronizar os agendamentos e impedir reservas concorrentes entre dispositivos.
- Definir e implementar a área profissional e suas permissões.
- Validar o escopo de cancelamento, confirmação de presença e demais mudanças de status.

A listagem atual apresenta os registros locais do aparelho, sem autenticação ou separação segura por pessoa. Room não substitui um serviço compartilhado nem resolve conflitos entre dispositivos.