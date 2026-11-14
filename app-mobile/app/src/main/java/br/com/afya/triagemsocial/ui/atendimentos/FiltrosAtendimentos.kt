package br.com.afya.triagemsocial.ui.atendimentos
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.afya.triagemsocial.data.repository.FiltroStatus
@Composable
fun FiltrosAtendimentos(filtro: FiltroStatus,
    erro: String?, selecionar: (FiltroStatus) -> Unit) {
    Row(Modifier.horizontalScroll(rememberScrollState())) {
        FiltroStatus.entries.forEach { item ->
            FilterChip(selected = filtro == item,
                onClick = { selecionar(item) },
                label = { Text(item.name) })
        }
    }
    erro?.let { Text(it, color = MaterialTheme.colorScheme.error) }
}
