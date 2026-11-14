package br.com.afya.triagemsocial.model


enum class StatusAgendamento(val titulo: String) {
    PENDENTE("Pendente"),
    AGENDADO("Agendado"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído")
}
