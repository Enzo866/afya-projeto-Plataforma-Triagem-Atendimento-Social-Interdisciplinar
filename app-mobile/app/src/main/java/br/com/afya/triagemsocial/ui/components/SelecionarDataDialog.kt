package br.com.afya.triagemsocial.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelecionarDataDialog(
    data: String,
    datasPermitidas: SelectableDates,
    onSelecionar: (String) -> Unit,
    onFechar: () -> Unit,
    anos: IntRange = DatePickerDefaults.YearRange
) {
    val formato = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).apply {
            timeZone = TimeZone.getTimeZone("UTC")
            isLenient = false
        }
    }
    val estado = rememberDatePickerState(
        initialSelectedDateMillis = remember(data, datasPermitidas) {
            val posicao = ParsePosition(0)
            formato.parse(data, posicao)?.time?.takeIf {
                posicao.index == data.length && datasPermitidas.isSelectableDate(it)
            }
        },
        yearRange = anos,
        selectableDates = datasPermitidas
    )
    val selecionada = estado.selectedDateMillis?.takeIf(datasPermitidas::isSelectableDate)
    DatePickerDialog(
        onDismissRequest = onFechar,
        confirmButton = {
            TextButton(
                enabled = selecionada != null,
                onClick = {
                    selecionada?.let { onSelecionar(formato.format(Date(it))) }
                    onFechar()
                }
            ) { Text("Selecionar") }
        },
        dismissButton = {
            TextButton(onClick = onFechar) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = estado, modifier = Modifier.fillMaxWidth())
    }
}
