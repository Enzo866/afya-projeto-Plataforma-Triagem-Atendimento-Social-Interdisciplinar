package br.com.afya.triagemsocial.ui.triagem

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.viewmodel.*

@Composable
fun CatalogoConteudo(
    state: CatalogoState,
    tentarNovamente: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Conheça os serviços",
            style = MaterialTheme.typography.titleMedium)
        when (state) {
            CatalogoState.Loading -> CircularProgressIndicator()
            is CatalogoState.Error -> {
                Text(state.mensagem,
                    color = MaterialTheme.colorScheme.error)
                Button(onClick = tentarNovamente) {
                    Text("Tentar novamente")
                }
            }
            is CatalogoState.Success -> {
                if (state.itens.isEmpty()) {
                    Text("Nenhum serviço disponível.")
                }
                state.itens.forEach { servico ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(servico.nome,
                                style = MaterialTheme.typography.titleSmall)
                            Text(servico.descricao)
                        }
                    }
                }
            }
        }
    }
}
