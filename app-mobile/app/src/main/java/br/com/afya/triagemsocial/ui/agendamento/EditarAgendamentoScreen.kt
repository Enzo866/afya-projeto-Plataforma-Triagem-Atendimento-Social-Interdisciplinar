package br.com.afya.triagemsocial.ui.agendamento
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.Agendamento
import br.com.afya.triagemsocial.ui.components.CamposDataHora

@Composable
fun EditarAgendamentoScreen(item: Agendamento, erro: String?,
    ocupado: Boolean, onSalvar: (String, String) -> Unit,
    onAlteracaoChange: (Boolean) -> Unit) {
    var data by rememberSaveable(item.id) { mutableStateOf(item.data) }
    var hora by rememberSaveable(item.id) { mutableStateOf(item.horario) }
    val alterado = data != item.data || hora != item.horario
    LaunchedEffect(alterado) { onAlteracaoChange(alterado) }
    Column(Modifier.fillMaxSize().imePadding()
        .verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(item.especialidade.nomeExibicao,
            style = MaterialTheme.typography.titleLarge)
        CamposDataHora(data, hora, !ocupado,
            selecionarData = { data = it },
            selecionarHorario = { hora = it })
        erro?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (ocupado) CircularProgressIndicator()
        Button(onClick = { onSalvar(data, hora) },
            enabled = !ocupado && alterado) { Text("Salvar alterações") }
    }
}
