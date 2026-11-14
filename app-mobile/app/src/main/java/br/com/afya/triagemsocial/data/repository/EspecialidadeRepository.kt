package br.com.afya.triagemsocial.data.repository

import br.com.afya.triagemsocial.data.remote.*
import br.com.afya.triagemsocial.model.Servico

interface EspecialidadeRepository {
    suspend fun listar(): List<Servico>
}

class ApiEspecialidadeRepository(
    private val remote: EspecialidadeRemoteDataSource
) : EspecialidadeRepository {
    override suspend fun listar(): List<Servico> =
        remote.listar().map { dto ->
            val id = dto.id
            val nome = dto.nome
            val descricao = dto.descricao
            require(!id.isNullOrBlank() && !nome.isNullOrBlank()
                && !descricao.isNullOrBlank()) {
                "Resposta do serviço incompleta."
            }
            Servico(id, nome, descricao)
        }
}
