package br.com.afya.triagemsocial.model

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

data class TriagemUiState(
    val nome: String = "",
    val dataNascimento: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val motivo: String = "",
    val especialidade: Especialidade? = null,
    val mensagemErro: String? = null,
    val nomeResponsavel: String = "",
    val telefoneResponsavel: String = ""
) {
    val idadeCalculada: Int?
        get() = calcularIdade(dataNascimento)

    val exigeResponsavel: Boolean
        get() = idadeCalculada?.let { it < 18 } == true

    val exigeTelefonePaciente: Boolean
        get() = idadeCalculada?.let { it >= 18 } == true
}

private val formatoNascimento = Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}")

internal fun calcularIdade(dataNascimento: String, hoje: Calendar = GregorianCalendar()): Int? {
    if (!formatoNascimento.matches(dataNascimento)) return null
    val (dia, mes, ano) = dataNascimento.split('/').map { it.toInt() }
    val nascimento = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
        clear()
        isLenient = false
        set(ano, mes - 1, dia)
    }
    try {
        nascimento.timeInMillis
    } catch (_: IllegalArgumentException) {
        return null
    }

    val antesDoAniversario = hoje.get(Calendar.MONTH) < mes - 1 ||
        (hoje.get(Calendar.MONTH) == mes - 1 && hoje.get(Calendar.DAY_OF_MONTH) < dia)
    val idade = hoje.get(Calendar.YEAR) - ano - if (antesDoAniversario) 1 else 0
    return idade.takeIf { it in 0..130 }
}

internal fun menorDeIdade(idade: String): Boolean =
    idadeValida(idade)?.let { it < 18 } == true

internal fun telefoneComDddValido(telefone: String): Boolean =
    telefone.length in 10..11 && telefone.all { it in '0'..'9' }

internal fun idadeValida(idade: String): Int? =
    idade.takeIf { it.isNotEmpty() && it.all { digito -> digito in '0'..'9' } }
        ?.toIntOrNull()?.takeIf { it in 0..130 }

internal fun erroDadosResponsavel(nome: String, telefone: String): String? = when {
    nome.isBlank() -> "Informe o nome do responsável."
    !telefoneComDddValido(telefone) -> "Informe o telefone do responsável com DDD."
    else -> null
}
