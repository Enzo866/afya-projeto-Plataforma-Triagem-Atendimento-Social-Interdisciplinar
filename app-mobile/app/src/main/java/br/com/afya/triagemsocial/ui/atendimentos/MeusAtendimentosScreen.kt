package br.com.afya.triagemsocial.ui.atendimentos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.Agendamento

@Composable
fun MeusAtendimentosScreen(
    agendamentos: List<Agendamento>,
    onVoltar: () -> Unit
) {

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
                text = "Meus Atendimentos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (agendamentos.isEmpty()) {

                Text(
                    text = "Nenhum atendimento agendado.",
                    color = MaterialTheme.colorScheme.onBackground
                )

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {

                    items(agendamentos) { agendamento ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),

                            colors = CardDefaults.cardColors(
                                containerColor =
                                    MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text =
                                        agendamento
                                            .especialidade
                                            .nomeExibicao,

                                    fontWeight = FontWeight.Bold,

                                    color =
                                        MaterialTheme.colorScheme
                                            .onSecondaryContainer
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text =
                                        "${agendamento.data} - " +
                                                agendamento.horario,

                                    color =
                                        MaterialTheme.colorScheme
                                            .onSecondaryContainer
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text =
                                        "Status: " +
                                                agendamento.status.name,

                                    color =
                                        MaterialTheme.colorScheme
                                            .onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Voltar")
            }
        }
    }
}