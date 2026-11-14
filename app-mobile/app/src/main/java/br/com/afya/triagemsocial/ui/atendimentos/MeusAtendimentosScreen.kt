package br.com.afya.triagemsocial.ui.atendimentos
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.Agendamento
import br.com.afya.triagemsocial.ui.components.ConfirmarAcaoDialog
import br.com.afya.triagemsocial.ui.components.IndicadorStatus

@Composable
fun MeusAtendimentosScreen(agendamentos: List<Agendamento>,
    erro: String?, ocupado: Boolean, onEditar: (Int) -> Unit,
    onExcluir: (Int) -> Unit, filtros: @Composable () -> Unit,
    onDetalhes: (Int) -> Unit) {
    var excluirId by rememberSaveable { mutableStateOf<Int?>(null) }
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)) {
        item { filtros() }
        erro?.let { mensagem ->
            item { Text(mensagem, color = MaterialTheme.colorScheme.error) }
        }
        if (agendamentos.isEmpty()) {
            item { Text("Nenhum atendimento para o filtro selecionado.") }
        }
        items(agendamentos, key = { it.id }) { item ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(item.especialidade.nomeExibicao,
                        style = MaterialTheme.typography.titleMedium)
                    Text("${item.data} - ${item.horario}")
                    IndicadorStatus(item.status, prefixo = "Status: ")
                    OutlinedButton(onClick = { onDetalhes(item.id) },
                        enabled = !ocupado) { Text("Ver detalhes") }
                    TextButton(onClick = { onEditar(item.id) },
                        enabled = !ocupado) { Text("Editar horário") }
                    TextButton(onClick = { excluirId = item.id },
                        enabled = !ocupado) { Text("Excluir") }
                }
            }
        }
    }
    excluirId?.let { id ->
        ConfirmarAcaoDialog("Excluir atendimento?",
            "O registro será removido do aparelho.", "Excluir",
            confirmar = {
                excluirId = null
                onExcluir(id)
            },
            cancelar = { excluirId = null })
    }
}
