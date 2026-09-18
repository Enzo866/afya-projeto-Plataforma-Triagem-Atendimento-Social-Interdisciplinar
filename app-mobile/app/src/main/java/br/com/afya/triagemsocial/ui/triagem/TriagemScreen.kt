package br.com.afya.triagemsocial.ui.triagem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.TriagemUiState

@Composable
fun TriagemScreen(
    uiState: TriagemUiState,
    onNomeChange: (String) -> Unit,
    onIdadeChange: (String) -> Unit,
    onCpfChange: (String) -> Unit,
    onMotivoChange: (String) -> Unit,
    onEspecialidadeChange: (Especialidade) -> Unit,
    onContinuar: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Text(
            text = "Triagem Social",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Informe seus dados e selecione a área de atendimento."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Dados pessoais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.nome,
                    onValueChange = onNomeChange,
                    label = {
                        Text("Nome")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.idade,
                    onValueChange = onIdadeChange,
                    label = {
                        Text("Idade")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.cpf,
                    onValueChange = onCpfChange,
                    label = {
                        Text("CPF")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.motivo,
                    onValueChange = onMotivoChange,
                    label = {
                        Text("Motivo do atendimento")
                    },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Área de atendimento",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    onEspecialidadeChange(
                        Especialidade.PSICOLOGIA
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Psicologia")
            }

            Button(
                onClick = {
                    onEspecialidadeChange(
                        Especialidade.DIREITO
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Direito")
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    onEspecialidadeChange(
                        Especialidade.FISIOTERAPIA
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Fisioterapia")
            }

            Button(
                onClick = {
                    onEspecialidadeChange(
                        Especialidade.FARMACIA
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Farmácia")
            }
        }

        if (uiState.especialidade != null) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text =
                    "Área selecionada: " +
                            uiState.especialidade.nomeExibicao,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        if (uiState.mensagemErro != null) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = uiState.mensagemErro,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onContinuar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar")
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}