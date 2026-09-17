package br.com.afya.triagemsocial.model

data class TriagemUiState(
    val nome: String = "",
    val idade: String = "",
    val cpf: String = "",
    val motivo: String = "",
    val especialidade: Especialidade? = null,
    val mensagemErro: String? = null
) {
    val formularioValido: Boolean
        get() =
            nome.isNotBlank() &&
                    idade.isNotBlank() &&
                    cpf.isNotBlank() &&
                    motivo.isNotBlank() &&
                    especialidade != null
}