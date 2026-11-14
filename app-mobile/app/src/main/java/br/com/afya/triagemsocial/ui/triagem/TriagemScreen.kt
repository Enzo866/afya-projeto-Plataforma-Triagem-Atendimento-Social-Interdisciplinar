package br.com.afya.triagemsocial.ui.triagem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.*
import br.com.afya.triagemsocial.ui.components.SelecionarDataDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun TriagemScreen(uiState: TriagemUiState,
    onNomeChange: (String) -> Unit,
    onDataNascimentoChange: (String) -> Unit,
    onNomeResponsavelChange: (String) -> Unit,
    onTelefoneResponsavelChange: (String) -> Unit,
    onCpfChange: (String) -> Unit,
    onTelefoneChange: (String) -> Unit,
    onMotivoChange: (String) -> Unit,
    onEspecialidadeChange: (Especialidade) -> Unit,
    onContinuar: () -> Unit) {
    Column(Modifier.fillMaxSize().imePadding()
        .verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Informe seus dados e selecione a área de atendimento.")
        Campo("Nome", uiState.nome, onNomeChange)
        CampoDataNascimento(uiState.dataNascimento, onDataNascimentoChange)
        Campo("CPF", uiState.cpf, onCpfChange, KeyboardType.Number)
        if (uiState.exigeTelefonePaciente) {
            Campo("Telefone com DDD", uiState.telefone, onTelefoneChange, KeyboardType.Phone)
        }
        if (uiState.exigeResponsavel) {
            Text("Dados do responsável", style = MaterialTheme.typography.titleMedium)
            Campo("Nome completo do responsável", uiState.nomeResponsavel, onNomeResponsavelChange)
            Campo("Telefone do responsável com DDD", uiState.telefoneResponsavel,
                onTelefoneResponsavelChange, KeyboardType.Phone)
        }
        OutlinedTextField(value = uiState.motivo,
            onValueChange = onMotivoChange,
            label = { Text("Motivo do atendimento") },
            minLines = 3, modifier = Modifier.fillMaxWidth())
        Text("Área de atendimento",
            style = MaterialTheme.typography.titleMedium)
        Column(Modifier.selectableGroup()) {
            Especialidade.entries.forEach { area ->
                val selecionada = uiState.especialidade == area
                Row(Modifier.fillMaxWidth()
                    .selectable(selecionada, role = Role.RadioButton) { onEspecialidadeChange(area) }
                    .heightIn(min = 48.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selecionada, onClick = null)
                    Spacer(Modifier.width(8.dp))
                    Text(area.nomeExibicao)
                }
            }
        }
        uiState.mensagemErro?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        Button(onClick = onContinuar,
            modifier = Modifier.fillMaxWidth()) { Text("Ir para agendamento") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampoDataNascimento(data: String, onSelecionar: (String) -> Unit) {
    var abrirCalendario by rememberSaveable { mutableStateOf(false) }
    OutlinedButton(
        onClick = { abrirCalendario = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(if (data.isBlank()) "Selecionar data de nascimento" else "Data de nascimento: $data")
    }

    if (abrirCalendario) {
        val hoje = remember { GregorianCalendar() }
        val anoAtual = hoje.get(Calendar.YEAR)
        val datasPermitidas = remember(hoje) {
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                    calcularIdade(formato.format(Date(utcTimeMillis)), hoje) != null
            }
        }
        SelecionarDataDialog(
            data = data,
            datasPermitidas = datasPermitidas,
            onSelecionar = onSelecionar,
            onFechar = { abrirCalendario = false },
            anos = (anoAtual - 131)..anoAtual
        )
    }
}

@Composable
private fun Campo(titulo: String, valor: String,
    alterar: (String) -> Unit, teclado: KeyboardType = KeyboardType.Text) {
    OutlinedTextField(value = valor, onValueChange = alterar,
        label = { Text(titulo) }, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = Modifier.fillMaxWidth())
}
