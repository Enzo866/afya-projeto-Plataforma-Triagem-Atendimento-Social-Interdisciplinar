package br.com.afya.triagemsocial.model

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

internal const val HORA_ABERTURA = 7
internal const val HORA_FECHAMENTO = 23
internal const val AVISO_HORARIO = "Escolha um horário das 07:00 às 23:00."

internal fun horarioPermitido(hora: Int, minuto: Int): Boolean =
    minuto in 0..59 &&
        (hora in HORA_ABERTURA until HORA_FECHAMENTO || (hora == HORA_FECHAMENTO && minuto == 0))

internal fun diaPermitido(inicio: Long, fuso: TimeZone = TimeZone.getDefault()): Boolean =
    calendarioDaAgenda(inicio, fuso).get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY

internal fun validarDisponibilidade(inicio: Long, fuso: TimeZone = TimeZone.getDefault()) {
    require(diaPermitido(inicio, fuso)) { "Não há agendamentos aos domingos." }
    val calendario = calendarioDaAgenda(inicio, fuso)
    require(horarioPermitido(calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE))) {
        AVISO_HORARIO
    }
}

private fun calendarioDaAgenda(inicio: Long, fuso: TimeZone): Calendar =
    GregorianCalendar(fuso).apply { timeInMillis = inicio }
