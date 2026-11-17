package br.com.afya.triagemsocial.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.StatusAgendamento
import br.com.afya.triagemsocial.ui.theme.StatusError
import br.com.afya.triagemsocial.ui.theme.StatusInfo
import br.com.afya.triagemsocial.ui.theme.StatusSuccess
import br.com.afya.triagemsocial.ui.theme.StatusWarning

@Composable
internal fun IndicadorStatus(status: StatusAgendamento, prefixo: String = "") {
    val temaEscuro = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val cor = when (status) {
        StatusAgendamento.PENDENTE -> if (temaEscuro) StatusWarning else Color(0xFF9C6900)
        StatusAgendamento.CANCELADO -> if (temaEscuro) Color(0xFFFF8A80) else StatusError
        StatusAgendamento.AGENDADO -> if (temaEscuro) Color(0xFF81C784) else StatusSuccess
        StatusAgendamento.CONCLUIDO -> if (temaEscuro) Color(0xFF90CAF9) else StatusInfo
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(10.dp).background(cor, CircleShape))
        Text(prefixo + status.titulo)
    }
}
