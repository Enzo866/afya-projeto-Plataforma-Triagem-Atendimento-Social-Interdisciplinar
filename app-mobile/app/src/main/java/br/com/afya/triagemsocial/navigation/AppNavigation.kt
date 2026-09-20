package br.com.afya.triagemsocial.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.afya.triagemsocial.ui.agendamento.AgendamentoScreen
import br.com.afya.triagemsocial.ui.atendimentos.MeusAtendimentosScreen
import br.com.afya.triagemsocial.ui.home.HomeScreen
import br.com.afya.triagemsocial.ui.resultado.ResultadoScreen
import br.com.afya.triagemsocial.ui.triagem.TriagemScreen
import br.com.afya.triagemsocial.viewmodel.AgendamentoViewModel
import br.com.afya.triagemsocial.viewmodel.TriagemViewModel

@Composable
fun AppNavigation(
    navController: NavHostController =
        rememberNavController(),

    triagemViewModel: TriagemViewModel =
        viewModel(),

    agendamentoViewModel: AgendamentoViewModel =
        viewModel()
) {

    val triagemState by
    triagemViewModel
        .uiState
        .collectAsStateWithLifecycle()

    val agendamentoState by
    agendamentoViewModel
        .uiState
        .collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            HomeScreen(

                onIniciarTriagem = {
                    navController.navigate(
                        "triagem"
                    )
                },

                onMeusAtendimentos = {
                    navController.navigate(
                        "meus_atendimentos"
                    )
                },

                onAcessoProfissional = {
                    // Tela profissional será
                    // adicionada futuramente.
                }
            )
        }

        composable("triagem") {

            TriagemScreen(

                uiState = triagemState,

                onNomeChange =
                    triagemViewModel::
                    onNomeChange,

                onIdadeChange =
                    triagemViewModel::
                    onIdadeChange,

                onCpfChange =
                    triagemViewModel::
                    onCpfChange,

                onTelefoneChange =
                    triagemViewModel::
                    onTelefoneChange,

                onMotivoChange =
                    triagemViewModel::
                    onMotivoChange,

                onEspecialidadeChange =
                    triagemViewModel::
                    onEspecialidadeChange,

                onContinuar = {

                    if (
                        triagemViewModel
                            .validarTriagem()
                    ) {

                        navController.navigate(
                            "resultado"
                        )
                    }
                }
            )
        }

        composable("resultado") {

            val especialidade =
                triagemState.especialidade

            if (
                especialidade != null
            ) {

                ResultadoScreen(

                    especialidade =
                        especialidade,

                    onAgendar = {
                        navController.navigate(
                            "agendamento"
                        )
                    },

                    onVoltar = {
                        navController
                            .popBackStack()
                    }
                )
            }
        }

        composable("agendamento") {

            AgendamentoScreen(

                uiState =
                    agendamentoState,

                datas =
                    agendamentoViewModel
                        .datasDisponiveis,

                horarios =
                    agendamentoViewModel
                        .horariosDisponiveis,

                onDataSelecionada =
                    agendamentoViewModel::
                    selecionarData,

                onHorarioSelecionado =
                    agendamentoViewModel::
                    selecionarHorario,

                onConfirmar = {

                    val especialidade =
                        triagemState
                            .especialidade

                    if (
                        especialidade != null
                    ) {

                        val sucesso =
                            agendamentoViewModel
                                .confirmarAgendamento(

                                    nome =
                                        triagemState
                                            .nome,

                                    cpf =
                                        triagemState
                                            .cpf,

                                    especialidade =
                                        especialidade
                                )

                        if (sucesso) {

                            navController.navigate(
                                "meus_atendimentos"
                            ) {

                                popUpTo(
                                    "home"
                                )
                            }
                        }
                    }
                },

                onVoltar = {
                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            "meus_atendimentos"
        ) {

            MeusAtendimentosScreen(

                agendamentos =
                    agendamentoState
                        .agendamentos,

                onVoltar = {
                    navController
                        .popBackStack()
                }
            )
        }
    }
}