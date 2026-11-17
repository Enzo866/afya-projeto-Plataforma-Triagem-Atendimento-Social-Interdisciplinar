package br.com.afya.triagemsocial.ui.profissional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import br.com.afya.triagemsocial.model.StatusAgendamento
import br.com.afya.triagemsocial.ui.components.IndicadorStatus
import br.com.afya.triagemsocial.viewmodel.ProfissionalUiState

@Composable
internal fun ProfissionalScreen(
    estado: ProfissionalUiState,
    onEntrar: (String, String) -> Unit,
    onAlterarStatus: (Int, StatusAgendamento) -> Unit
) {
    val area = estado.area
    if (area == null) {
        LoginProfissional(estado.erro, onEntrar)
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text(area.nomeExibicao, style = MaterialTheme.typography.headlineSmall) }
        estado.erro?.let { erro ->
            item { Text(erro, color = MaterialTheme.colorScheme.error) }
        }
        if (estado.carregando) {
            item { CircularProgressIndicator() }
        } else if (estado.agendamentos.isEmpty() && estado.erro == null) {
            item { Text("Nenhum agendamento para esta área.") }
        }
        items(estado.agendamentos, key = { it.id }) { atendimento ->
            Card(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(atendimento.nome, style = MaterialTheme.typography.titleMedium)
                    if (atendimento.pacienteMenor) {
                        val responsavel = atendimento.nomeResponsavel.orEmpty().ifBlank { "Não informado" }
                        val telefone = formatarTelefone(atendimento.telefoneResponsavel.orEmpty())
                        Text("Responsável: $responsavel")
                        Text("Telefone do responsável: $telefone")
                    } else {
                        Text("Telefone: ${formatarTelefone(atendimento.telefone)}")
                    }
                    Text("${atendimento.data} às ${atendimento.horario}")
                    IndicadorStatus(atendimento.status, prefixo = "Status atual: ")
                    var aberto by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(
                            onClick = { aberto = true },
                            enabled = !estado.salvando
                        ) { Text("Alterar status") }
                        DropdownMenu(expanded = aberto, onDismissRequest = { aberto = false }) {
                            StatusAgendamento.entries.forEach { status ->
                                DropdownMenuItem(
                                    text = { IndicadorStatus(status) },
                                    enabled = status != atendimento.status && !estado.salvando,
                                    onClick = {
                                        aberto = false
                                        onAlterarStatus(atendimento.id, status)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatarTelefone(telefone: String): String {
    if (telefone.isBlank()) return "Não informado"
    if (telefone.length !in 10..11 || telefone.any { it !in '0'..'9' }) return telefone
    return "(${telefone.take(2)}) ${telefone.drop(2).dropLast(4)}-${telefone.takeLast(4)}"
}

@Composable
private fun LoginProfissional(erro: String?, onEntrar: (String, String) -> Unit) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().imePadding()
            .verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Entre com a conta da sua área de atendimento.")
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuário") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        erro?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = {
                onEntrar(usuario, senha)
                senha = ""
            },
            enabled = usuario.isNotBlank() && senha.isNotEmpty()
        ) { Text("Entrar") }
    }
}
