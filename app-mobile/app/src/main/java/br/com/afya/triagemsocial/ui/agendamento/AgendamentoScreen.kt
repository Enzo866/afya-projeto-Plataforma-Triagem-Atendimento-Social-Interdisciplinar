package br.com.afya.triagemsocial.ui.agendamento

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.Agendamento
import br.com.afya.triagemsocial.viewmodel.AgendamentoUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Composable
fun AgendamentoScreen(
    uiState: AgendamentoUiState,
    datas: List<String>,
    horarios: List<String>,
    onDataSelecionada: (String) -> Unit,
    onHorarioSelecionado: (String) -> Unit,
    onConfirmar: () -> Unit,
    onVoltar: () -> Unit
) {

    val context = LocalContext.current

    var mensagemErro by remember {
        mutableStateOf<String?>(null)
    }

    val calendarioAtual = Calendar.getInstance()

    /*
     * DATE PICKER
     *
     * Permite escolher qualquer data a partir de hoje.
     */
    val datePickerDialog = DatePickerDialog(
        context,
        { _, ano, mes, dia ->

            val dataFormatada = String.format(
                Locale.getDefault(),
                "%02d/%02d/%04d",
                dia,
                mes + 1,
                ano
            )

            onDataSelecionada(dataFormatada)

            // Limpa eventual erro anterior.
            mensagemErro = null
        },
        calendarioAtual.get(Calendar.YEAR),
        calendarioAtual.get(Calendar.MONTH),
        calendarioAtual.get(Calendar.DAY_OF_MONTH)
    ).apply {

        // Impede selecionar dias anteriores ao dia atual.
        datePicker.minDate =
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
    }

    /*
     * TIME PICKER
     *
     * Permite selecionar hora e minuto.
     */
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hora, minuto ->

            val horarioFormatado = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                hora,
                minuto
            )

            onHorarioSelecionado(horarioFormatado)

            // Limpa eventual erro anterior.
            mensagemErro = null
        },
        calendarioAtual.get(Calendar.HOUR_OF_DAY),
        calendarioAtual.get(Calendar.MINUTE),
        true
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp)
        ) {

            Text(
                text = "Agendamento",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =============================
            // DATA
            // =============================

            Text(
                text = "Data do atendimento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedButton(
                onClick = {
                    datePickerDialog.show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        if (uiState.dataSelecionada.isBlank()) {
                            "Selecionar data"
                        } else {
                            uiState.dataSelecionada
                        }
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =============================
            // HORÁRIO
            // =============================

            Text(
                text = "Horário do atendimento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedButton(
                onClick = {
                    timePickerDialog.show()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.dataSelecionada.isNotBlank()
            ) {

                Text(
                    text =
                        if (uiState.horarioSelecionado.isBlank()) {
                            "Selecionar horário"
                        } else {
                            uiState.horarioSelecionado
                        }
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =============================
            // INFORMAÇÃO SOBRE A TRAVA
            // =============================

            Text(
                text = "Cada atendimento reserva o horário por 1 hora.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    "Exemplo: um atendimento às 14:00 " +
                            "bloqueia novos agendamentos até às 15:00.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // =============================
            // ERRO
            // =============================

            if (mensagemErro != null) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = mensagemErro!!,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            // =============================
            // CONFIRMAR
            // =============================

            Button(
                onClick = {

                    val data = uiState.dataSelecionada
                    val horario = uiState.horarioSelecionado

                    when {

                        data.isBlank() -> {

                            mensagemErro =
                                "Selecione uma data para o atendimento."
                        }

                        horario.isBlank() -> {

                            mensagemErro =
                                "Selecione um horário para o atendimento."
                        }

                        horarioNoPassado(
                            data = data,
                            horario = horario
                        ) -> {

                            mensagemErro =
                                "Não é possível agendar um atendimento no passado."
                        }

                        existeConflitoDeHorario(
                            novaData = data,
                            novoHorario = horario,
                            agendamentos =
                                uiState.agendamentos
                        ) -> {

                            mensagemErro =
                                "Esse horário não está disponível. " +
                                        "Existe outro atendimento dentro " +
                                        "do intervalo de 1 hora."
                        }

                        else -> {

                            mensagemErro = null

                            onConfirmar()
                        }
                    }
                },

                enabled =
                    uiState.dataSelecionada.isNotBlank() &&
                            uiState.horarioSelecionado.isNotBlank(),

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Confirmar Agendamento"
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Voltar"
                )
            }
        }
    }
}

/*
 * Verifica se o novo horário entra em conflito
 * com algum atendimento existente.
 *
 * Cada atendimento ocupa 60 minutos.
 *
 */
private fun existeConflitoDeHorario(
    novaData: String,
    novoHorario: String,
    agendamentos: List<Agendamento>
): Boolean {

    val novoInicio =
        converterParaMillis(
            data = novaData,
            horario = novoHorario
        ) ?: return true

    val umaHoraEmMillis =
        60 * 60 * 1000L

    return agendamentos.any { agendamento ->

        /*
         * Agendamentos cancelados não bloqueiam horário.
         *
         * Usamos status.name para não depender do nome
         * exato da classe do enum atual do projeto.
         */
        if (
            agendamento.status.name.equals(
                "CANCELADO",
                ignoreCase = true
            )
        ) {
            return@any false
        }

        val inicioExistente =
            converterParaMillis(
                data = agendamento.data,
                horario = agendamento.horario
            ) ?: return@any false

        /*
         * Calcula a diferença entre o horário novo
         * e o horário já agendado.
         */
        val diferenca =
            abs(
                novoInicio -
                        inicioExistente
            )

        /*
         * Menos de 60 minutos significa conflito.
         */
        diferenca < umaHoraEmMillis
    }
}

/*
 * Impede agendamento em data/horário anterior
 * ao momento atual.
 */
private fun horarioNoPassado(
    data: String,
    horario: String
): Boolean {

    val horarioSelecionado =
        converterParaMillis(
            data = data,
            horario = horario
        ) ?: return true

    return horarioSelecionado <
            System.currentTimeMillis()
}

/*
 * Converte para milissegundos.
 */
private fun converterParaMillis(
    data: String,
    horario: String
): Long? {

    return try {

        val formato =
            SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
            )

        formato.isLenient = false

        formato.parse(
            "$data $horario"
        )?.time

    } catch (
        exception: Exception
    ) {

        null
    }
}