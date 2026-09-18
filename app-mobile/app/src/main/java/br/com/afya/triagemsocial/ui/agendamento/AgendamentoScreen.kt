package br.com.afya.triagemsocial.ui.agendamento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.viewmodel.AgendamentoUiState

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Agendamento",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Escolha uma data:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        datas.forEach { data ->

            OutlinedButton(
                onClick = {
                    onDataSelecionada(data)
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                val prefixo =
                    if (uiState.dataSelecionada == data) {
                        "✓ "
                    } else {
                        ""
                    }

                Text("$prefixo$data")
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Escolha um horário:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        horarios.chunked(3).forEach { linha ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                linha.forEach { horario ->

                    OutlinedButton(
                        onClick = {
                            onHorarioSelecionado(horario)
                        },
                        modifier = Modifier.weight(1f)
                    ) {

                        val prefixo =
                            if (
                                uiState.horarioSelecionado ==
                                horario
                            ) {
                                "✓ "
                            } else {
                                ""
                            }

                        Text("$prefixo$horario")
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onConfirmar,
            enabled =
                uiState.dataSelecionada.isNotBlank() &&
                        uiState.horarioSelecionado.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar Agendamento")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedButton(
            onClick = onVoltar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar")
        }
    }
}