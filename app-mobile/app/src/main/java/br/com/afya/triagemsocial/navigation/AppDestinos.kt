package br.com.afya.triagemsocial.navigation
import androidx.annotation.DrawableRes
import br.com.afya.triagemsocial.R

enum class AppDestino(
    val rota: String, val titulo: String, @DrawableRes val icone: Int
) {
    INICIO("home", "Início", R.drawable.ic_menu_inicio),
    ATENDIMENTOS("meus_atendimentos", "Atendimentos",
        R.drawable.ic_menu_atendimentos),
    SERVICOS("servicos", "Serviços", R.drawable.ic_menu_servicos)
}
