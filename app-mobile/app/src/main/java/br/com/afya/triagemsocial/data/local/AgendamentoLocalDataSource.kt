package br.com.afya.triagemsocial.data.local

import androidx.room.withTransaction

class AgendamentoLocalDataSource(private val db: AppDatabase) {
    private val dao = db.agendamentos()
    fun observar() = dao.observar()
    suspend fun buscar(id: Int) = dao.buscar(id)

    suspend fun salvar(item: AgendamentoEntity): Int =
        db.withTransaction {
            if (item.id != 0) {
                require(dao.buscar(item.id) != null) {
                    "Atendimento não encontrado."
                }
            }
            if (item.status != "CANCELADO") {
                require(item.inicio > System.currentTimeMillis()) {
                    "Escolha um horário futuro."
                }
                val duracao = 60 * 60 * 1000L
                require(!dao.existeConflito(
                    item.especialidade, item.id,
                    item.inicio - duracao, item.inicio + duracao
                )) { "Já existe atendimento nesse intervalo." }
            }
            if (item.id == 0) dao.inserir(item).toInt()
            else {
                check(dao.atualizar(item) == 1)
                item.id
            }
        }

    suspend fun excluir(id: Int) {
        require(dao.excluir(id) == 1) {
            "Atendimento não encontrado."
        }
    }
}