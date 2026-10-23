package br.com.afya.triagemsocial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.StatusAgendamento

@Entity(tableName = "agendamentos")
data class AgendamentoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val cpf: String,
    val idade: String,
    val telefone: String,
    val motivo: String,
    val especialidade: String,
    val inicio: Long,
    val status: String
)

data class AgendamentoDados(
    val nome: String,
    val cpf: String,
    val idade: String,
    val telefone: String,
    val motivo: String,
    val especialidade: Especialidade,
    val inicio: Long,
    val status: StatusAgendamento = StatusAgendamento.AGENDADO
)

fun AgendamentoDados.entidade(id: Int = 0) = AgendamentoEntity(
    id, nome.trim(), cpf, idade, telefone, motivo.trim(),
    especialidade.name, inicio, status.name
)