package br.com.afya.triagemsocial.ui.agendamento
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.ui.components.CamposDataHora
import br.com.afya.triagemsocial.viewmodel.AgendamentoUiState

@Composable
fun AgendamentoScreen(uiState: AgendamentoUiState,
    areaAtendimento: String?,
    onDataSelecionada: (String) -> Unit,
    onHorarioSelecionado: (String) -> Unit,
    onConfirmar: () -> Unit) {
    Column(Modifier.fillMaxSize().imePadding()
        .verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (areaAtendimento == null) {
            Text("Volte à triagem para selecionar a área de atendimento.")
            return@Column
        }
        Text("Área de atendimento: $areaAtendimento")
        CamposDataHora(uiState.dataSelecionada,
            uiState.horarioSelecionado, !uiState.salvando,
            onDataSelecionada, onHorarioSelecionado)
        uiState.erro?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        if (uiState.salvando) CircularProgressIndicator()
        Button(onClick = onConfirmar,
            enabled = !uiState.salvando &&
                uiState.dataSelecionada.isNotBlank() &&
                uiState.horarioSelecionado.isNotBlank(),
            modifier = Modifier.fillMaxWidth()) {
            Text("Confirmar agendamento")
        }
    }
}
