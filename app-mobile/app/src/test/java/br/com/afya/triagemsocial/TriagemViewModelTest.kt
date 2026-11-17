package br.com.afya.triagemsocial

import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.calcularIdade
import br.com.afya.triagemsocial.viewmodel.TriagemViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

class TriagemViewModelTest {
    @Test
    fun menorDeIdadePrecisaInformarNomeETelefoneDoResponsavel() {
        val triagem = triagemPreenchida(nascimentoHaAnos(17))
        assertTrue(triagem.uiState.value.exigeResponsavel)
        assertFalse(triagem.uiState.value.exigeTelefonePaciente)
        assertFalse(triagem.validarTriagem())
        assertEquals("Informe o nome do responsável.", triagem.uiState.value.mensagemErro)

        triagem.onNomeResponsavelChange("Maria da Silva")
        triagem.onTelefoneResponsavelChange("99999-9999")
        assertFalse(triagem.validarTriagem())
        assertEquals(
            "Informe o telefone do responsável com DDD.",
            triagem.uiState.value.mensagemErro
        )

        triagem.onTelefoneResponsavelChange("(46) 99999-9999")
        assertTrue(triagem.validarTriagem())
    }

    @Test
    fun pessoaComDezoitoAnosPodeAgendarSemResponsavel() {
        val triagem = triagemPreenchida(nascimentoHaAnos(17))
        assertFalse(triagem.validarTriagem())

        triagem.onDataNascimentoChange(nascimentoHaAnos(18))
        assertFalse(triagem.uiState.value.exigeResponsavel)
        assertTrue(triagem.uiState.value.exigeTelefonePaciente)
        assertFalse(triagem.validarTriagem())
        triagem.onTelefoneChange("46988888888")
        assertTrue(triagem.validarTriagem())
    }

    @Test
    fun dataDeNascimentoInvalidaImpedeAgendamento() {
        val triagem = triagemPreenchida("")
        for (data in listOf("", "31/04/2000", "29/02/2001", "01/01/9999", "1/1/2000")) {
            triagem.onDataNascimentoChange(data)
            assertFalse(triagem.uiState.value.exigeResponsavel)
            assertFalse("Data aceita: $data", triagem.validarTriagem())
        }
    }

    @Test
    fun idadeMudaNoDiaDoAniversario() {
        val hoje = GregorianCalendar(2026, Calendar.NOVEMBER, 14)
        assertEquals(17, calcularIdade("15/11/2008", hoje))
        assertEquals(18, calcularIdade("14/11/2008", hoje))
        assertEquals(18, calcularIdade("13/11/2008", hoje))
        assertEquals(0, calcularIdade("14/11/2026", hoje))
        assertNull(calcularIdade("15/11/2026", hoje))
    }

    private fun nascimentoHaAnos(anos: Int): String {
        val nascimento = GregorianCalendar().apply { add(Calendar.YEAR, -anos) }
        return SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(nascimento.time)
    }

    private fun triagemPreenchida(dataNascimento: String) = TriagemViewModel().apply {
        onNomeChange("João da Silva")
        onDataNascimentoChange(dataNascimento)
        onCpfChange("12345678901")
        onMotivoChange("Atendimento")
        onEspecialidadeChange(Especialidade.PSICOLOGIA)
    }
}
