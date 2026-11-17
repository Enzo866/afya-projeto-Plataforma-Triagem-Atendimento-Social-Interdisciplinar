package br.com.afya.triagemsocial.data.repository

import br.com.afya.triagemsocial.model.Especialidade
import java.util.Locale

internal class AcessoProfissionalDemo(contas: Array<String>) {
    private val credenciais = contas.associate { conta ->
        val (usuario, senha, area) = conta.split(':')
        usuario to (senha to Especialidade.valueOf(area))
    }

    val disponivel: Boolean get() = credenciais.isNotEmpty()

    fun autenticar(usuario: String, senha: String): Especialidade? {
        val conta = credenciais[usuario.trim().lowercase(Locale.ROOT)]
        return conta?.takeIf { it.first == senha }?.second
    }
}
