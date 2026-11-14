package br.com.afya.triagemsocial.ui.home
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun HomeScreen(onIniciarTriagem: () -> Unit, onAcessoProfissional: (() -> Unit)?) {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Triagem e agendamento de atendimentos sociais")
            Text("Psicologia, Fisioterapia, Farmácia e Direito")
            Button(onClick = onIniciarTriagem) { Text("Iniciar triagem") }
            onAcessoProfissional?.let {
                OutlinedButton(onClick = it) { Text("Acesso profissional") }
            }
        }
    }
}
