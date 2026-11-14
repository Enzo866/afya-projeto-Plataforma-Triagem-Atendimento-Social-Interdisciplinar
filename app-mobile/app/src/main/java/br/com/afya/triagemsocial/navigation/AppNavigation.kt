package br.com.afya.triagemsocial.navigation
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import br.com.afya.triagemsocial.R
import br.com.afya.triagemsocial.data.local.*
import br.com.afya.triagemsocial.data.remote.CatalogoContainer
import br.com.afya.triagemsocial.data.repository.*
import br.com.afya.triagemsocial.model.TriagemUiState
import br.com.afya.triagemsocial.ui.agendamento.*
import br.com.afya.triagemsocial.ui.atendimentos.*
import br.com.afya.triagemsocial.ui.components.*
import br.com.afya.triagemsocial.ui.home.HomeScreen
import br.com.afya.triagemsocial.ui.profissional.ProfissionalScreen
import br.com.afya.triagemsocial.ui.triagem.*
import br.com.afya.triagemsocial.viewmodel.*

fun <VM : ViewModel> fabrica(tipo: Class<VM>, criar: () -> VM)
    : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == tipo)
        return criar() as T
    }
}
private fun TriagemUiState.possuiDados() =
    nome.isNotBlank() || dataNascimento.isNotBlank() || cpf.isNotBlank() ||
        telefone.isNotBlank() || motivo.isNotBlank() || especialidade != null ||
        nomeResponsavel.isNotBlank() || telefoneResponsavel.isNotBlank()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    triagemViewModel: TriagemViewModel = viewModel()
) {
    val appContext = LocalContext.current.applicationContext
    val repo = remember(appContext) {
        RoomAgendamentoRepository(
            AgendamentoLocalDataSource(AppDatabase.obter(appContext)))
    }
    val factory = remember(repo) {
        fabrica(AgendamentoViewModel::class.java) {
            AgendamentoViewModel(repo)
        }
    }
    val vm: AgendamentoViewModel = viewModel(factory = factory)
    val acessoProfissional = remember(appContext) {
        AcessoProfissionalDemo(appContext.resources.getStringArray(R.array.contas_profissionais_demo))
    }
    val profissionalFactory = remember(acessoProfissional, repo) {
        fabrica(ProfissionalViewModel::class.java) {
            ProfissionalViewModel(acessoProfissional, repo)
        }
    }
    val profissional: ProfissionalViewModel = viewModel(factory = profissionalFactory)
    val sessaoProfissional by profissional.uiState.collectAsStateWithLifecycle()
    val triagem by triagemViewModel.uiState.collectAsStateWithLifecycle()
    val estado by vm.uiState.collectAsStateWithLifecycle()
    val prefsFactory = remember(appContext) {
        fabrica(PreferenciasViewModel::class.java) {
            PreferenciasViewModel(PreferenciasRepository(
                PreferenciasDataSource(appContext)))
        }
    }
    val prefs: PreferenciasViewModel = viewModel(factory = prefsFactory)
    val filtro by prefs.filtro.collectAsStateWithLifecycle()
    val erroFiltro by prefs.erro.collectAsStateWithLifecycle()
    val entrada by navController.currentBackStackEntryAsState()
    val rota = entrada?.destination?.route
    val emAcessoProfissional = rota == "profissional"
    val raiz = AppDestino.entries.any { it.rota == rota }
    val voltarAoInicio: () -> Unit = {
        vm.limparErro()
        if (navController.currentDestination?.route != AppDestino.INICIO.rota &&
            !navController.popBackStack(AppDestino.INICIO.rota, inclusive = false)
        ) {
            navController.navigate(AppDestino.INICIO.rota) {
                popUpTo(navController.graph.id)
                launchSingleTop = true
            }
        }
    }
    var edicaoAlterada by rememberSaveable(entrada?.id) {
        mutableStateOf(false)
    }
    var confirmarDescarte by rememberSaveable(entrada?.id) {
        mutableStateOf(false)
    }
    val voltar: () -> Unit = {
        if (emAcessoProfissional) {
            if (!profissional.uiState.value.salvando) {
                profissional.sair()
                navController.popBackStack()
            }
        } else if (!estado.salvando && rota == AppDestino.ATENDIMENTOS.rota) {
            voltarAoInicio()
        } else if (!estado.salvando) {
            val precisaConfirmar = when (rota) {
                "triagem" -> triagem.possuiDados()
                "agendamento" -> estado.dataSelecionada.isNotBlank() ||
                    estado.horarioSelecionado.isNotBlank()
                "editar/{id}" -> edicaoAlterada
                else -> false
            }
            if (precisaConfirmar) confirmarDescarte = true
            else {
                vm.limparErro()
                navController.popBackStack()
            }
        }
    }
    val titulo = when (rota) {
        "home" -> "Afya Conecta"
        "triagem" -> "Nova solicitação"
        "agendamento" -> "Agendamento"
        "meus_atendimentos" -> "Meus atendimentos"
        "editar/{id}" -> "Editar horário"
        "detalhes/{id}" -> "Detalhes do atendimento"
        "servicos" -> "Conhecer serviços"
        "profissional" -> "Acesso profissional"
        else -> "Afya Conecta"
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(titulo) }, navigationIcon = {
                if ((!raiz && rota != null) || rota == AppDestino.ATENDIMENTOS.rota) {
                    TextButton(onClick = voltar,
                        enabled = !estado.salvando && !sessaoProfissional.salvando) {
                        Text(if (emAcessoProfissional && sessaoProfissional.area != null) "Sair" else "Voltar")
                    }
                }
            })
        },
        bottomBar = {
            if (raiz) MenuPrincipal(rota, !estado.salvando) { destino ->
                if (destino.rota != rota) {
                    if (destino == AppDestino.INICIO) {
                        voltarAoInicio()
                    } else {
                        vm.limparErro()
                        navController.navigate(destino.rota) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
    ) { espacos ->
        NavHost(navController, startDestination = "home",
            modifier = Modifier.fillMaxSize().padding(espacos)
                .consumeWindowInsets(espacos)) {
            composable("home") {
                HomeScreen(
                    onIniciarTriagem = {
                        vm.limparErro()
                        navController.navigate("triagem")
                    },
                    onAcessoProfissional = if (acessoProfissional.disponivel) {
                        { navController.navigate("profissional") { launchSingleTop = true } }
                    } else null)
            }
            if (acessoProfissional.disponivel) {
                composable("profissional") {
                    ProfissionalScreen(
                        sessaoProfissional, profissional::entrar, profissional::alterarStatus
                    )
                }
            }
            composable("triagem") {
                TriagemScreen(uiState = triagem,
                    onNomeChange = triagemViewModel::onNomeChange,
                    onDataNascimentoChange = triagemViewModel::onDataNascimentoChange,
                    onNomeResponsavelChange = triagemViewModel::onNomeResponsavelChange,
                    onTelefoneResponsavelChange = triagemViewModel::onTelefoneResponsavelChange,
                    onCpfChange = triagemViewModel::onCpfChange,
                    onTelefoneChange = triagemViewModel::onTelefoneChange,
                    onMotivoChange = triagemViewModel::onMotivoChange,
                    onEspecialidadeChange =
                        triagemViewModel::onEspecialidadeChange,
                    onContinuar = {
                        if (triagemViewModel.validarTriagem()) {
                            vm.limparErro()
                            navController.navigate("agendamento") { launchSingleTop = true }
                        }
                    })
            }
            composable("agendamento") {
                AgendamentoScreen(estado, triagem.especialidade?.nomeExibicao, vm::selecionarData,
                    vm::selecionarHorario, onConfirmar = {
                        if (!triagemViewModel.validarTriagem()) {
                            navController.popBackStack()
                            return@AgendamentoScreen
                        }
                        val area = triagem.especialidade
                        val idade = triagem.idadeCalculada
                        if (area != null && idade != null) {
                            val inicio = try {
                                inicioEmMillis(estado.dataSelecionada,
                                    estado.horarioSelecionado)
                            } catch (e: IllegalArgumentException) {
                                vm.informarErro(e.message ?: "Revise o horário.")
                                null
                            }
                            if (inicio != null) vm.salvar(AgendamentoDados(
                                nome = triagem.nome, cpf = triagem.cpf,
                                idade = idade.toString(),
                                telefone = if (idade >= 18) triagem.telefone else "",
                                motivo = triagem.motivo,
                                especialidade = area, inicio = inicio,
                                nomeResponsavel = triagem.nomeResponsavel.takeIf { idade < 18 },
                                telefoneResponsavel = triagem.telefoneResponsavel.takeIf { idade < 18 }
                            )) {
                                triagemViewModel.limparTriagem()
                                navController.navigate(AppDestino.ATENDIMENTOS.rota) {
                                    popUpTo(AppDestino.INICIO.rota) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    })
            }
            composable("meus_atendimentos") {
                MeusAtendimentosScreen(
                    agendamentos = estado.agendamentos.filter {
                        filtro.aceita(it.status)
                    }, erro = estado.erro, ocupado = estado.salvando,
                    onEditar = {
                        vm.limparErro()
                        navController.navigate("editar/$it")
                    }, onExcluir = vm::excluir, filtros = {
                        FiltrosAtendimentos(filtro, erroFiltro,
                            prefs::selecionar)
                    }, onDetalhes = {
                        navController.navigate("detalhes/$it")
                    })
            }
            composable("detalhes/{id}") { registro ->
                val id = registro.arguments?.getString("id")?.toIntOrNull()
                val item = estado.agendamentos.firstOrNull { it.id == id }
                DetalhesAtendimentoScreen(item, estado.salvando,
                    onEditar = {
                        if (item != null) {
                            vm.limparErro()
                            navController.navigate("editar/${item.id}")
                        }
                    })
            }
            composable("editar/{id}") { registro ->
                val id = registro.arguments?.getString("id")?.toIntOrNull()
                val item = estado.agendamentos.firstOrNull { it.id == id }
                if (item == null) {
                    DetalhesAtendimentoScreen(null, estado.salvando,
                        onEditar = {})
                } else EditarAgendamentoScreen(item,
                    estado.erro, estado.salvando,
                    onSalvar = { data, hora ->
                        vm.reagendar(item.id, data, hora) {
                            edicaoAlterada = false
                            navController.popBackStack()
                        }
                    },
                    onAlteracaoChange = { edicaoAlterada = it })
            }
            composable("servicos") {
                val catalogoFactory = remember {
                    fabrica(CatalogoViewModel::class.java) {
                        CatalogoViewModel(CatalogoContainer.repository)
                    }
                }
                val catalogo: CatalogoViewModel =
                    viewModel(factory = catalogoFactory)
                val servicos by
                    catalogo.state.collectAsStateWithLifecycle()
                Column(Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState()).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CatalogoConteudo(servicos, catalogo::carregar)
                }
            }
        }
    }
    BackHandler(enabled = (!raiz && rota != null) || rota == AppDestino.ATENDIMENTOS.rota || estado.salvando) {
        voltar()
    }
    if (confirmarDescarte) ConfirmarAcaoDialog(
        titulo = "Descartar alterações?",
        mensagem = "Os dados alterados nesta etapa não serão salvos.",
        textoConfirmar = "Descartar",
        confirmar = {
            confirmarDescarte = false
            when (rota) {
                "triagem" -> {
                    triagemViewModel.limparTriagem()
                    vm.limparSelecao()
                }
                "agendamento" -> vm.limparSelecao()
                "editar/{id}" -> vm.limparErro()
            }
            edicaoAlterada = false
            navController.popBackStack()
        }, cancelar = { confirmarDescarte = false })
}
