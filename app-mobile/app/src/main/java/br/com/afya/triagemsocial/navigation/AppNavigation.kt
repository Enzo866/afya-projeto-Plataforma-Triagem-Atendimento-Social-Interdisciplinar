package br.com.afya.triagemsocial.navigation
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.data.remote.CatalogoContainer
import br.com.afya.triagemsocial.ui.triagem.CatalogoConteudo
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import br.com.afya.triagemsocial.data.local.*
import br.com.afya.triagemsocial.data.repository.*
import br.com.afya.triagemsocial.ui.agendamento.*
import br.com.afya.triagemsocial.ui.atendimentos.*
import br.com.afya.triagemsocial.ui.home.HomeScreen
import br.com.afya.triagemsocial.ui.resultado.ResultadoScreen
import br.com.afya.triagemsocial.ui.triagem.TriagemScreen
import br.com.afya.triagemsocial.viewmodel.*
fun <VM : ViewModel> fabrica(tipo: Class<VM>, criar: () -> VM)
        : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == tipo)
        return criar() as T
    }
}
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    triagemViewModel: TriagemViewModel = viewModel()
) {
    val appContext = LocalContext.current.applicationContext
    val factory = remember(appContext) {
        val repo = RoomAgendamentoRepository(
            AgendamentoLocalDataSource(AppDatabase.obter(appContext)))
        fabrica(AgendamentoViewModel::class.java) {
            AgendamentoViewModel(repo)
        }
    }
    val vm: AgendamentoViewModel = viewModel(factory = factory)
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
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onIniciarTriagem = { navController.navigate("triagem") },
                onMeusAtendimentos = {
                    navController.navigate("meus_atendimentos")
                }, onConhecerServicos = { navController.navigate("servicos") })
        }
        composable("triagem") {
            TriagemScreen(uiState = triagem,
                onNomeChange = triagemViewModel::onNomeChange,
                onIdadeChange = triagemViewModel::onIdadeChange,
                onCpfChange = triagemViewModel::onCpfChange,
                onTelefoneChange = triagemViewModel::onTelefoneChange,
                onMotivoChange = triagemViewModel::onMotivoChange,
                onEspecialidadeChange = triagemViewModel::onEspecialidadeChange,
                onContinuar = {
                    if (triagemViewModel.validarTriagem()) {
                        navController.navigate("resultado")
                    }
                })
        }
        composable("resultado") {
            triagem.especialidade?.let { area ->
                ResultadoScreen(area,
                    onAgendar = { navController.navigate("agendamento") },
                    onVoltar = { navController.popBackStack() })
            }
        }
        composable("agendamento") {
            AgendamentoScreen(estado, vm::selecionarData,
                vm::selecionarHorario, onConfirmar = {
                    val area = triagem.especialidade
                    if (area != null) {
                        val inicio = try {
                            inicioEmMillis(estado.dataSelecionada,
                                estado.horarioSelecionado)
                        } catch (e: IllegalArgumentException) {
                            vm.informarErro(e.message ?: "Revise data e horário.")
                            null
                        }
                        if (inicio != null) vm.salvar(AgendamentoDados(
                            nome = triagem.nome, cpf = triagem.cpf,
                            idade = triagem.idade, telefone = triagem.telefone,
                            motivo = triagem.motivo, especialidade = area,
                            inicio = inicio)) {
                            triagemViewModel.limparTriagem()
                            navController.navigate("meus_atendimentos") {
                                popUpTo("home")
                                launchSingleTop = true
                            }
                        }
                    }
                }, onVoltar = { navController.popBackStack() })
        }
        composable("meus_atendimentos") {
            MeusAtendimentosScreen(
                agendamentos = estado.agendamentos.filter { filtro.aceita(it.status) },
                erro = estado.erro, ocupado = estado.salvando,
                onEditar = { navController.navigate("editar/$it") },
                onExcluir = vm::excluir,
                onVoltar = { navController.popBackStack() },
                filtros = { FiltrosAtendimentos(filtro, erroFiltro,
                    prefs::selecionar) })
        }
        composable("editar/{id}") { rota ->
            val id = rota.arguments?.getString("id")?.toIntOrNull()
            val item = estado.agendamentos.firstOrNull { it.id == id }
            if (item == null) Text("Carregando atendimento...")
            else EditarAgendamentoScreen(item, estado.erro, estado.salvando,
                onSalvar = { data, hora ->
                    vm.reagendar(item.id, data, hora) {
                        navController.popBackStack()
                    }
                }, onVoltar = { navController.popBackStack() })
        }
        composable("servicos") {
            val catalogoFactory = remember {
                fabrica(CatalogoViewModel::class.java) {
                    CatalogoViewModel(CatalogoContainer.repository)
                }
            }
            val catalogo: CatalogoViewModel = viewModel(
                factory = catalogoFactory)
            val servicos by catalogo.state.collectAsStateWithLifecycle()
            Surface(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize().systemBarsPadding()
                    .verticalScroll(rememberScrollState()).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CatalogoConteudo(servicos, catalogo::carregar)
                    OutlinedButton(onClick = {
                        navController.popBackStack()
                    }) { Text("Voltar") }
                }
            }
        }
    }
}
