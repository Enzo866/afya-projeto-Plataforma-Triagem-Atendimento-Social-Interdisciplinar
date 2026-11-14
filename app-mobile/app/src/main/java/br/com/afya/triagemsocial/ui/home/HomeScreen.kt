package br.com.afya.triagemsocial.ui.home
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun HomeScreen(onIniciarTriagem: () -> Unit,
               onMeusAtendimentos: () -> Unit,
               onConhecerServicos: () -> Unit) {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().systemBarsPadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("AFYA CONECTA",
                style = MaterialTheme.typography.headlineMedium)
            Text("Triagem e agendamento de atendimentos sociais")
            Text("Psicologia, Fisioterapia, Farmácia e Direito")
            Button(onClick = onIniciarTriagem) { Text("Iniciar triagem") }
            OutlinedButton(onClick = onMeusAtendimentos) {
                Text("Meus atendimentos")
            }
            OutlinedButton(onClick = onConhecerServicos) {
                Text("Conhecer serviços")
            }
        }
    }
}
