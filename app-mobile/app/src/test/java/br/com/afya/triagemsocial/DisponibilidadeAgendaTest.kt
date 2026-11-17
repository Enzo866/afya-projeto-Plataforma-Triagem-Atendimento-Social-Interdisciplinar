package br.com.afya.triagemsocial

import br.com.afya.triagemsocial.model.diaPermitido
import br.com.afya.triagemsocial.model.validarDisponibilidade
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

class DisponibilidadeAgendaTest {
    private val fuso = TimeZone.getTimeZone("America/Sao_Paulo")

    @Test
    fun aceitaLimitesPermitidosNoSabado() {
        validarDisponibilidade(inicio(14, 7, 0), fuso)
        validarDisponibilidade(inicio(14, 22, 59), fuso)
        validarDisponibilidade(inicio(14, 23, 0), fuso)
    }

    @Test
    fun bloqueiaDomingoMesmoEmHorarioDiurno() {
        assertFalse(diaPermitido(inicio(15, 12, 0), fuso))
        assertThrows(IllegalArgumentException::class.java) {
            validarDisponibilidade(inicio(15, 12, 0), fuso)
        }
    }

    @Test
    fun bloqueiaAntesDasSeteEDepoisDasVinteETres() {
        for ((hora, minuto) in listOf(0 to 0, 6 to 59, 23 to 1, 23 to 59)) {
            assertThrows(IllegalArgumentException::class.java) {
                validarDisponibilidade(inicio(16, hora, minuto), fuso)
            }
        }
    }

    private fun inicio(dia: Int, hora: Int, minuto: Int): Long =
        GregorianCalendar(fuso).apply {
            clear()
            set(2026, Calendar.NOVEMBER, dia, hora, minuto)
        }.timeInMillis
}
