package br.com.afya.triagemsocial.viewmodel

import androidx.lifecycle.ViewModel
import br.com.afya.triagemsocial.model.Agendamento
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.StatusAgendamento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AgendamentoUiState(
    val dataSelecionada: String = "",
    val horarioSelecionado: String = "",
    val agendamentos: List<Agendamento> = emptyList()
)

class AgendamentoViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(AgendamentoUiState())

    val uiState: StateFlow<AgendamentoUiState> =
        _uiState.asStateFlow()

    val datasDisponiveis = listOf(
        "20/09/2026",
        "21/09/2026",
        "22/09/2026"
    )

    val horariosDisponiveis = listOf(
        "08:00",
        "09:00",
        "10:00",
        "14:00",
        "15:00"
    )

    fun selecionarData(data: String) {
        _uiState.update {
            it.copy(dataSelecionada = data)
        }
    }

    fun selecionarHorario(horario: String) {
        _uiState.update {
            it.copy(horarioSelecionado = horario)
        }
    }

    fun confirmarAgendamento(
        nome: String,
        cpf: String,
        especialidade: Especialidade
    ): Boolean {

        val estado = _uiState.value

        if (
            estado.dataSelecionada.isBlank() ||
            estado.horarioSelecionado.isBlank()
        ) {
            return false
        }

        val novoAgendamento = Agendamento(
            id = estado.agendamentos.size + 1,
            nome = nome,
            cpf = cpf,
            especialidade = especialidade,
            data = estado.dataSelecionada,
            horario = estado.horarioSelecionado,
            status = StatusAgendamento.AGENDADO
        )

        _uiState.update {
            it.copy(
                agendamentos =
                    it.agendamentos + novoAgendamento,
                dataSelecionada = "",
                horarioSelecionado = ""
            )
        }

        return true
    }
}