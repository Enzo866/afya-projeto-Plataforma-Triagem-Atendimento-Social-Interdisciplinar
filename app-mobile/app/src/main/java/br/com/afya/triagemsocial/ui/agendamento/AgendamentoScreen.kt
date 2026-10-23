package br.com.afya.triagemsocial.ui.agendamento

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.viewmodel.AgendamentoUiState

@Composable
fun AgendamentoScreen(uiState: AgendamentoUiState,
                      onDataSelecionada: (String) -> Unit,
                      onHorarioSelecionado: (String) -> Unit,
                      onConfirmar: () -> Unit, onVoltar: () -> Unit) {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().systemBarsPadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Agendamento",
                style = MaterialTheme.typography.headlineMedium)
            Text("Informe uma data e um horário futuros.")
            OutlinedTextField(uiState.dataSelecionada,
                onDataSelecionada, label = { Text("Data: dd/MM/aaaa") },
                modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(uiState.horarioSelecionado,
                onHorarioSelecionado, label = { Text("Horário: HH:mm") },
                modifier = Modifier.fillMaxWidth(), singleLine = true)
            uiState.erro?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Button(onClick = onConfirmar, enabled = !uiState.salvando) {
                Text(if (uiState.salvando) "Salvando..." else "Confirmar")
            }
            OutlinedButton(onClick = onVoltar,
                enabled = !uiState.salvando) { Text("Voltar") }
        }
    }
}