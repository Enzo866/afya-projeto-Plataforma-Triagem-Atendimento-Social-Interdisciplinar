package br.com.afya.triagemsocial.ui.atendimentos
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.Agendamento
import br.com.afya.triagemsocial.ui.components.IndicadorStatus

@Composable
fun DetalhesAtendimentoScreen(item: Agendamento?, ocupado: Boolean,
    onEditar: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (item == null) {
            Text("Atendimento indisponível.")
            Text("Volte à lista e selecione um registro existente.")
        } else {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(item.especialidade.nomeExibicao,
                        style = MaterialTheme.typography.titleLarge)
                    Text("Solicitante: ${item.nome}")
                    Text("Data: ${item.data}")
                    Text("Horário: ${item.horario}")
                    IndicadorStatus(item.status, prefixo = "Status: ")
                }
            }
            Button(onClick = onEditar,
                enabled = !ocupado) { Text("Editar horário") }
        }
    }
}
