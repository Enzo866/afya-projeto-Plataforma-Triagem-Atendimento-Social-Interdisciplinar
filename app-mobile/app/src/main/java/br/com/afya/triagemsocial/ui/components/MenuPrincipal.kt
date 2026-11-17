package br.com.afya.triagemsocial.ui.components
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import br.com.afya.triagemsocial.navigation.AppDestino

@Composable
fun MenuPrincipal(rotaAtual: String?, habilitado: Boolean,
    selecionar: (AppDestino) -> Unit) {
    NavigationBar {
        AppDestino.entries.forEach { destino ->
            NavigationBarItem(
                selected = rotaAtual == destino.rota,
                enabled = habilitado,
                onClick = { selecionar(destino) },
                icon = {
                    Icon(painterResource(destino.icone),
                        contentDescription = null)
                }, label = { Text(destino.titulo) })
        }
    }
}
