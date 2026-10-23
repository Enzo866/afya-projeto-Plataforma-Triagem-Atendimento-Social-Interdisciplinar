package br.com.afya.triagemsocial.data.repository

import br.com.afya.triagemsocial.data.local.*
import br.com.afya.triagemsocial.model.Agendamento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AgendamentoRepository {
    fun observar(): Flow<List<Agendamento>>
    suspend fun buscar(id: Int): AgendamentoEntity?
    suspend fun salvar(dados: AgendamentoDados, id: Int = 0): Int
    suspend fun excluir(id: Int)
}

class RoomAgendamentoRepository(
    private val local: AgendamentoLocalDataSource
) : AgendamentoRepository {
    override fun observar() = local.observar().map { lista ->
        lista.map { it.modelo() }
    }
    override suspend fun buscar(id: Int) = local.buscar(id)

    override suspend fun salvar(
        dados: AgendamentoDados, id: Int
    ): Int {
        require(dados.nome.isNotBlank()) { "Informe o nome." }
        require(dados.cpf.matches(Regex("[0-9]{11}"))) {
            "Informe CPF com 11 dígitos."
        }
        require(dados.telefone.matches(Regex("[0-9]{10,11}"))) {
            "Informe telefone com DDD."
        }
        require(dados.idade.toIntOrNull()?.let { it in 0..130 }
                == true) { "Informe uma idade válida." }
        require(dados.motivo.isNotBlank()) { "Informe o motivo." }
        return local.salvar(dados.entidade(id))
    }
    override suspend fun excluir(id: Int) = local.excluir(id)
}