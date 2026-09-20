package br.com.afya.triagemsocial.viewmodel

import androidx.lifecycle.ViewModel
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.TriagemUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TriagemViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TriagemUiState())

    val uiState: StateFlow<TriagemUiState> =
        _uiState.asStateFlow()

    fun onNomeChange(nome: String) {
        _uiState.update {
            it.copy(
                nome = nome,
                mensagemErro = null
            )
        }
    }

    fun onIdadeChange(idade: String) {
        _uiState.update {
            it.copy(
                idade = idade.filter { caractere ->
                    caractere.isDigit()
                },
                mensagemErro = null
            )
        }
    }

    fun onCpfChange(cpf: String) {
        _uiState.update {
            it.copy(
                cpf = cpf
                    .filter { caractere ->
                        caractere.isDigit()
                    }
                    .take(11),

                mensagemErro = null
            )
        }
    }

    fun onTelefoneChange(telefone: String) {
        _uiState.update {
            it.copy(
                telefone = telefone
                    .filter { caractere ->
                        caractere.isDigit()
                    }
                    .take(11),

                mensagemErro = null
            )
        }
    }

    fun onMotivoChange(motivo: String) {
        _uiState.update {
            it.copy(
                motivo = motivo,
                mensagemErro = null
            )
        }
    }

    fun onEspecialidadeChange(
        especialidade: Especialidade
    ) {
        _uiState.update {
            it.copy(
                especialidade = especialidade,
                mensagemErro = null
            )
        }
    }

    fun validarTriagem(): Boolean {

        val estadoAtual = _uiState.value

        if (estadoAtual.nome.isBlank()) {

            atualizarErro(
                "Informe o nome."
            )

            return false
        }

        if (estadoAtual.idade.isBlank()) {

            atualizarErro(
                "Informe a idade."
            )

            return false
        }

        if (estadoAtual.cpf.length != 11) {

            atualizarErro(
                "Informe um CPF com 11 dígitos."
            )

            return false
        }

        if (
            estadoAtual.telefone.length !in 10..11
        ) {

            atualizarErro(
                "Informe um telefone válido com DDD."
            )

            return false
        }

        if (estadoAtual.motivo.isBlank()) {

            atualizarErro(
                "Informe o motivo do atendimento."
            )

            return false
        }

        if (estadoAtual.especialidade == null) {

            atualizarErro(
                "Selecione uma área de atendimento."
            )

            return false
        }

        return true
    }

    private fun atualizarErro(
        mensagem: String
    ) {

        _uiState.update {
            it.copy(
                mensagemErro = mensagem
            )
        }
    }

    fun limparTriagem() {
        _uiState.value = TriagemUiState()
    }
}