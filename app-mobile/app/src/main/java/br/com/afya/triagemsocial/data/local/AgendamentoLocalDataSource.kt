package br.com.afya.triagemsocial.data.local

import androidx.room.withTransaction
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.StatusAgendamento
import br.com.afya.triagemsocial.model.validarDisponibilidade

class AgendamentoLocalDataSource(private val db: AppDatabase) {
    private val dao = db.agendamentos()
    fun observar() = dao.observar()
    suspend fun buscar(id: Int) = dao.buscar(id)
    fun observarDaArea(area: Especialidade) = dao.observarDaArea(area.name)

    suspend fun salvar(item: AgendamentoEntity): Int =
        db.withTransaction {
            validarDisponibilidade(item.inicio)
            val registro = if (item.id == 0) item else {
                val atual = requireNotNull(dao.buscar(item.id)) {
                    "Atendimento não encontrado."
                }
                item.copy(status = atual.status)
            }
            if (registro.status != StatusAgendamento.CANCELADO.name) {
                require(registro.inicio > System.currentTimeMillis()) {
                    "Escolha um horário futuro."
                }
                verificarConflito(registro)
            }
            if (registro.id == 0) dao.inserir(registro).toInt()
            else {
                check(dao.atualizar(registro) == 1)
                registro.id
            }
        }

    suspend fun alterarStatus(area: Especialidade, id: Int, status: StatusAgendamento) {
        db.withTransaction {
            val registro = requireNotNull(dao.buscarDaArea(id, area.name)) {
                "Atendimento indisponível para esta área."
            }
            if (registro.status == StatusAgendamento.CANCELADO.name &&
                status != StatusAgendamento.CANCELADO
            ) {
                if (status == StatusAgendamento.PENDENTE || status == StatusAgendamento.AGENDADO) {
                    validarDisponibilidade(registro.inicio)
                }
                verificarConflito(registro)
            }
            check(dao.atualizarStatusDaArea(id, area.name, status.name) == 1)
        }
    }

    private suspend fun verificarConflito(item: AgendamentoEntity) {
        val duracao = 60 * 60 * 1000L
        require(!dao.existeConflito(
            item.especialidade, item.id,
            item.inicio - duracao, item.inicio + duracao
        )) { "Já existe atendimento nesse intervalo." }
    }

    suspend fun excluir(id: Int) {
        require(dao.excluir(id) == 1) {
            "Atendimento não encontrado."
        }
    }
}
