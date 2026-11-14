package br.com.afya.triagemsocial.data.repository

import br.com.afya.triagemsocial.data.local.*
import br.com.afya.triagemsocial.model.Agendamento
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.StatusAgendamento
import br.com.afya.triagemsocial.model.erroDadosResponsavel
import br.com.afya.triagemsocial.model.idadeValida
import br.com.afya.triagemsocial.model.telefoneComDddValido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AgendamentoRepository {
    fun observar(): Flow<List<Agendamento>>
    suspend fun buscar(id: Int): AgendamentoEntity?
    suspend fun salvar(dados: AgendamentoDados, id: Int = 0): Int
    suspend fun excluir(id: Int)
    fun observarDaArea(area: Especialidade): Flow<List<Agendamento>>
    suspend fun alterarStatus(area: Especialidade, id: Int, status: StatusAgendamento)
}

class RoomAgendamentoRepository(
    private val local: AgendamentoLocalDataSource
) : AgendamentoRepository {
    override fun observar() = local.observar().map { lista ->
        lista.map { it.modelo() }
    }
    override suspend fun buscar(id: Int) = local.buscar(id)
    override fun observarDaArea(area: Especialidade) =
        local.observarDaArea(area).map { lista -> lista.map { it.modelo() } }

    override suspend fun alterarStatus(area: Especialidade, id: Int, status: StatusAgendamento) =
        local.alterarStatus(area, id, status)

    override suspend fun salvar(
        dados: AgendamentoDados, id: Int
    ): Int {
        require(dados.nome.isNotBlank()) { "Informe o nome." }
        require(dados.cpf.matches(Regex("[0-9]{11}"))) {
            "Informe CPF com 11 dígitos."
        }
        val idade = requireNotNull(idadeValida(dados.idade)) { "Informe uma idade válida." }
        if (idade >= 18) {
            require(telefoneComDddValido(dados.telefone)) { "Informe telefone com DDD." }
        }
        require(dados.motivo.isNotBlank()) { "Informe o motivo." }
        if (id == 0 && idade < 18) {
            erroDadosResponsavel(
                dados.nomeResponsavel.orEmpty(), dados.telefoneResponsavel.orEmpty()
            )?.let { throw IllegalArgumentException(it) }
        }
        val entidade = dados.entidade(id)
        return local.salvar(
            when {
                idade >= 18 -> entidade.copy(nomeResponsavel = null, telefoneResponsavel = null)
                id == 0 -> entidade.copy(telefone = "")
                else -> entidade
            }
        )
    }
    override suspend fun excluir(id: Int) = local.excluir(id)
}
