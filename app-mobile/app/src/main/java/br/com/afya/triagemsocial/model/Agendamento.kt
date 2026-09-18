package br.com.afya.triagemsocial.model

data class Agendamento(
    val id: Int,
    val nome: String,
    val cpf: String,
    val especialidade: Especialidade,
    val data: String,
    val horario: String,
    val status: StatusAgendamento
)