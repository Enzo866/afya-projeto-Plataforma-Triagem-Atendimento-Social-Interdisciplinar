package br.com.afya.triagemsocial.ui.components
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ConfirmarAcaoDialog(titulo: String, mensagem: String,
    textoConfirmar: String, confirmar: () -> Unit, cancelar: () -> Unit) {
    AlertDialog(onDismissRequest = cancelar,
        title = { Text(titulo) }, text = { Text(mensagem) },
        confirmButton = {
            TextButton(onClick = confirmar) { Text(textoConfirmar) }
        }, dismissButton = {
            TextButton(onClick = cancelar) { Text("Cancelar") }
        })
}
