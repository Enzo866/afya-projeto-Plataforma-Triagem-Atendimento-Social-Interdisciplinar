package br.com.afya.triagemsocial.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface

@Composable
fun HomeScreen(
    onIniciarTriagem: () -> Unit,
    onMeusAtendimentos: () -> Unit,
    onAcessoProfissional: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            Text(
                text = "AFYA CONECTA",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Plataforma Integrada de Triagem e Agendamento de Atendimentos",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Atendimentos disponíveis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Psicologia • Fisioterapia • Farmácia • Direito"
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(
                onClick = onIniciarTriagem,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Triagem")
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedButton(
                onClick = onMeusAtendimentos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Meus Atendimentos")
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedButton(
                onClick = onAcessoProfissional,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Acesso Profissional")
            }
        }
    }
}