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

@Composable
fun MeusAtendimentosScreen(agendamentos: List<Agendamento>,
                           erro: String?, ocupado: Boolean, onEditar: (Int) -> Unit,
                           onExcluir: (Int) -> Unit, onVoltar: () -> Unit,
                           filtros: @Composable () -> Unit = {}) {
    var excluirId by rememberSaveable { mutableStateOf<Int?>(null) }
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().systemBarsPadding().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Meus atendimentos",
                style = MaterialTheme.typography.headlineMedium)
            filtros()
            erro?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (agendamentos.isEmpty()) Text("Nenhum atendimento nesta lista.")
            LazyColumn(Modifier.weight(1f)) {
                items(agendamentos, key = { it.id }) { item ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(item.especialidade.nomeExibicao)
                            Text("${item.data} - ${item.horario}")
                            Text("Status: ${item.status.name}")
                            Row {
                                TextButton(onClick = { onEditar(item.id) },
                                    enabled = !ocupado) { Text("Editar horário") }
                                TextButton(onClick = { excluirId = item.id },
                                    enabled = !ocupado) { Text("Excluir") }
                            }
                        }
                    }
                }
            }
            OutlinedButton(onClick = onVoltar) { Text("Voltar") }
        }
    }
    excluirId?.let { id ->
        AlertDialog(onDismissRequest = { excluirId = null },
            title = { Text("Excluir atendimento?") },
            text = { Text("O registro será removido do aparelho.") },
            confirmButton = {
                TextButton(onClick = {
                    excluirId = null
                    onExcluir(id)
                }) { Text("Excluir") }
            }, dismissButton = {
                TextButton(onClick = { excluirId = null }) { Text("Cancelar") }
            })
    }
}