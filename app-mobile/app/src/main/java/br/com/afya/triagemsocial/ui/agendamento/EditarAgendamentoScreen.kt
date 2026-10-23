package br.com.afya.triagemsocial.ui.agendamento

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.Agendamento
@Composable
fun EditarAgendamentoScreen(item: Agendamento, erro: String?,
                            ocupado: Boolean, onSalvar: (String, String) -> Unit,
                            onVoltar: () -> Unit) {
    var data by rememberSaveable(item.id) { mutableStateOf(item.data) }
    var hora by rememberSaveable(item.id) { mutableStateOf(item.horario) }
    var confirmarSaida by rememberSaveable { mutableStateOf(false) }
    val alterado = data != item.data || hora != item.horario
    val voltar = {
        if (alterado) confirmarSaida = true else onVoltar()
    }
    BackHandler { if (!ocupado) voltar() }
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().systemBarsPadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Editar horário",
                style = MaterialTheme.typography.headlineMedium)
            Text(item.especialidade.nomeExibicao)
            OutlinedTextField(data, { data = it },
                label = { Text("Data: dd/MM/aaaa") }, singleLine = true)
            OutlinedTextField(hora, { hora = it },
                label = { Text("Horário: HH:mm") }, singleLine = true)
            erro?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Button(onClick = { onSalvar(data, hora) }, enabled = !ocupado) {
                Text(if (ocupado) "Salvando..." else "Salvar")
            }
            OutlinedButton(onClick = voltar, enabled = !ocupado) {
                Text("Voltar")
            }
        }
    }
    if (confirmarSaida) AlertDialog(
        onDismissRequest = { confirmarSaida = false },
        title = { Text("Descartar alterações?") },
        text = { Text("O horário alterado ainda não foi salvo.") },
        confirmButton = {
            TextButton(onClick = onVoltar) { Text("Descartar") }
        }, dismissButton = {
            TextButton(onClick = { confirmarSaida = false }) {
                Text("Continuar editando")
            }
        })
}
