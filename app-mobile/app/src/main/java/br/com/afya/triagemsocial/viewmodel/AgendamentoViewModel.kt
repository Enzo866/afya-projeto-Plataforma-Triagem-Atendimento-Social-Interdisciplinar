package br.com.afya.triagemsocial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.triagemsocial.data.local.AgendamentoDados
import br.com.afya.triagemsocial.data.repository.AgendamentoRepository
import br.com.afya.triagemsocial.model.Agendamento
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AgendamentoUiState(
    val dataSelecionada: String = "",
    val horarioSelecionado: String = "",
    val agendamentos: List<Agendamento> = emptyList(),
    val salvando: Boolean = false,
    val erro: String? = null
)

class AgendamentoViewModel(
    private val repository: AgendamentoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AgendamentoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observar().catch {
                if (it is CancellationException) throw it
                _uiState.update { s -> s.copy(
                    erro = "Não foi possível ler os atendimentos."
                ) }
            }.collect { lista ->
                _uiState.update { it.copy(agendamentos = lista) }
            }
        }
    }
    fun selecionarData(data: String) {
        _uiState.update { it.copy(dataSelecionada = data) }
    }
    fun selecionarHorario(horario: String) {
        _uiState.update { it.copy(horarioSelecionado = horario) }
    }
    fun salvar(
        dados: AgendamentoDados,
        id: Int = 0,
        aoConcluir: () -> Unit = {}
    ) = executar(aoConcluir) {
        repository.salvar(dados, id)
        _uiState.update { it.copy(
            dataSelecionada = "", horarioSelecionado = ""
        ) }
    }

    fun reagendar(id: Int, data: String, hora: String,
                  aoConcluir: () -> Unit) = executar(aoConcluir) {
        val a = requireNotNull(repository.buscar(id)) {
            "Atendimento não encontrado."
        }
        repository.salvar(AgendamentoDados(a.nome, a.cpf,
            a.idade, a.telefone, a.motivo,
            br.com.afya.triagemsocial.model.Especialidade
                .valueOf(a.especialidade),
            br.com.afya.triagemsocial.data.local
                .inicioEmMillis(data, hora),
            br.com.afya.triagemsocial.model.StatusAgendamento
                .valueOf(a.status)), id)
    }
    fun excluir(id: Int) = executar({}) {
        repository.excluir(id)
    }

    fun informarErro(mensagem: String) {
        _uiState.update { it.copy(erro = mensagem) }
    }

    private fun executar(
        aoConcluir: () -> Unit,
        acao: suspend () -> Unit
    ) {
        if (_uiState.value.salvando) return
        _uiState.update { it.copy(salvando = true, erro = null) }
        viewModelScope.launch {
            var sucesso = false
            try {
                acao()
                sucesso = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalArgumentException) {
                informarErro(e.message ?: "Revise os dados.")
            } catch (e: Exception) {
                informarErro("Não foi possível salvar a alteração.")
            } finally {
                _uiState.update { it.copy(salvando = false) }
            }
            if (sucesso) aoConcluir()
        }
    }
}