package br.com.afya.triagemsocial.data.local

import br.com.afya.triagemsocial.model.*
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun AgendamentoEntity.modelo(): Agendamento = Agendamento(
    id = id,
    nome = nome,
    cpf = cpf,
    especialidade = Especialidade.valueOf(especialidade),
    data = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
        .format(Date(inicio)),
    horario = SimpleDateFormat("HH:mm", Locale.ROOT)
        .format(Date(inicio)),
    status = StatusAgendamento.valueOf(status)
)

fun inicioEmMillis(data: String, horario: String): Long {
    val texto = "$data $horario"
    require(Regex("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}")
        .matches(texto)) { "Selecione data e horário válidos." }
    val formato = SimpleDateFormat(
        "dd/MM/yyyy HH:mm", Locale.ROOT
    ).apply { isLenient = false }
    val posicao = ParsePosition(0)
    val instante = formato.parse(texto, posicao)
    require(instante != null && posicao.index == texto.length) {
        "Selecione data e horário válidos."
    }
    return instante.time
}