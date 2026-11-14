package br.com.afya.triagemsocial.viewmodel

import androidx.lifecycle.ViewModel
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.TriagemUiState
import br.com.afya.triagemsocial.model.erroDadosResponsavel
import br.com.afya.triagemsocial.model.telefoneComDddValido
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

    fun onDataNascimentoChange(dataNascimento: String) {
        _uiState.update {
            it.copy(
                dataNascimento = dataNascimento,
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

    fun onNomeResponsavelChange(nome: String) {
        _uiState.update { it.copy(nomeResponsavel = nome, mensagemErro = null) }
    }

    fun onTelefoneResponsavelChange(telefone: String) {
        _uiState.update {
            it.copy(
                telefoneResponsavel = telefone.filter { digito -> digito in '0'..'9' }.take(11),
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

        if (estadoAtual.dataNascimento.isBlank()) {

            atualizarErro(
                "Informe a data de nascimento."
            )

            return false
        }

        val idade = estadoAtual.idadeCalculada
        if (idade == null) {
            atualizarErro("Selecione uma data de nascimento válida.")
            return false
        }

        if (idade < 18) {
            val erro = erroDadosResponsavel(
                estadoAtual.nomeResponsavel, estadoAtual.telefoneResponsavel
            )
            if (erro != null) {
                atualizarErro(erro)
                return false
            }
        }

        if (estadoAtual.cpf.length != 11) {

            atualizarErro(
                "Informe um CPF com 11 dígitos."
            )

            return false
        }

        if (
            idade >= 18 && !telefoneComDddValido(estadoAtual.telefone)
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
