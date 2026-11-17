package br.com.afya.triagemsocial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.triagemsocial.data.repository.AcessoProfissionalDemo
import br.com.afya.triagemsocial.data.repository.AgendamentoRepository
import br.com.afya.triagemsocial.model.Agendamento
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.StatusAgendamento
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal data class ProfissionalUiState(
    val area: Especialidade? = null,
    val agendamentos: List<Agendamento> = emptyList(),
    val carregando: Boolean = false,
    val salvando: Boolean = false,
    val erro: String? = null
)

internal class ProfissionalViewModel(
    private val acesso: AcessoProfissionalDemo,
    private val repository: AgendamentoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfissionalUiState())
    val uiState = _uiState.asStateFlow()
    private var observacao: Job? = null

    fun entrar(usuario: String, senha: String) {
        if (_uiState.value.area != null) return
        val area = acesso.autenticar(usuario, senha)
        if (area == null) {
            _uiState.update { it.copy(erro = "Usuário ou senha inválidos.") }
            return
        }

        _uiState.value = ProfissionalUiState(area = area, carregando = true)
        observacao = viewModelScope.launch {
            repository.observarDaArea(area)
                .catch {
                    _uiState.update { estado ->
                        estado.copy(carregando = false, erro = "Não foi possível carregar a agenda.")
                    }
                }
                .collect { lista ->
                    _uiState.update { it.copy(agendamentos = lista, carregando = false) }
                }
        }
    }

    fun sair() {
        if (_uiState.value.salvando) return
        observacao?.cancel()
        observacao = null
        _uiState.value = ProfissionalUiState()
    }

    fun alterarStatus(id: Int, status: StatusAgendamento) {
        val estado = _uiState.value
        val area = estado.area ?: return
        if (estado.salvando) return
        _uiState.update { it.copy(salvando = true, erro = null) }
        viewModelScope.launch {
            try {
                repository.alterarStatus(area, id, status)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalArgumentException) {
                _uiState.update { it.copy(erro = e.message) }
            } catch (_: Exception) {
                _uiState.update { it.copy(erro = "Não foi possível alterar o status.") }
            } finally {
                _uiState.update { it.copy(salvando = false) }
            }
        }
    }
}
