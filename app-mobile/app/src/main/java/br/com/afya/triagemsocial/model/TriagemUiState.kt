package br.com.afya.triagemsocial.model

data class TriagemUiState(
    val nome: String = "",
    val idade: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val motivo: String = "",
    val especialidade: Especialidade? = null,
    val mensagemErro: String? = null
) {

    val formularioValido: Boolean
        get() =
            nome.isNotBlank() &&
                    idade.isNotBlank() &&
                    cpf.length == 11 &&
                    telefone.length in 10..11 &&
                    motivo.isNotBlank() &&
                    especialidade != null
}