package br.com.afya.triagemsocial.ui.components
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.AVISO_HORARIO
import br.com.afya.triagemsocial.model.HORA_ABERTURA
import br.com.afya.triagemsocial.model.diaPermitido
import br.com.afya.triagemsocial.model.horarioPermitido
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamposDataHora(data: String, horario: String,
    habilitado: Boolean = true,
    selecionarData: (String) -> Unit,
    selecionarHorario: (String) -> Unit) {
    var abrirData by rememberSaveable { mutableStateOf(false) }
    var abrirHora by rememberSaveable { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = { abrirData = true },
            enabled = habilitado, modifier = Modifier.fillMaxWidth()) {
            Text(if (data.isBlank()) "Selecionar data" else "Data: $data")
        }
        OutlinedButton(onClick = { abrirHora = true },
            enabled = habilitado, modifier = Modifier.fillMaxWidth()) {
            Text(if (horario.isBlank()) "Selecionar horário"
                else "Horário: $horario")
        }
        Text("Segunda a sábado, das 07:00 às 23:00.",
            style = MaterialTheme.typography.bodySmall)
    }
    if (abrirData) {
        val diasDisponiveis = remember {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                    diaPermitido(utcTimeMillis, TimeZone.getTimeZone("UTC"))
            }
        }
        SelecionarDataDialog(data, diasDisponiveis, selecionarData, onFechar = { abrirData = false })
    }
    if (abrirHora) {
        val agora = Calendar.getInstance()
        val partes = horario.split(":")
        val hora = (partes.getOrNull(0)?.toIntOrNull() ?: agora.get(Calendar.HOUR_OF_DAY))
        val minuto = partes.getOrNull(1)?.toIntOrNull()
            ?.takeIf { it in 0..59 } ?: agora.get(Calendar.MINUTE)
        val inicialPermitido = horarioPermitido(hora, minuto)
        val estado = rememberTimePickerState(
            initialHour = if (inicialPermitido) hora else HORA_ABERTURA,
            initialMinute = if (inicialPermitido) minuto else 0,
            is24Hour = true)
        val permitido = horarioPermitido(estado.hour, estado.minute)
        AlertDialog(onDismissRequest = { abrirHora = false },
            title = { Text("Selecionar horário") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.horizontalScroll(rememberScrollState())) {
                        TimeInput(state = estado)
                    }
                    if (!permitido) {
                        Text(AVISO_HORARIO, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                TextButton(enabled = permitido, onClick = {
                    selecionarHorario(String.format(Locale.ROOT,
                        "%02d:%02d", estado.hour, estado.minute))
                    abrirHora = false
                }) { Text("Selecionar") }
            }, dismissButton = {
                TextButton(onClick = { abrirHora = false }) {
                    Text("Cancelar")
                }
            })
    }
}
