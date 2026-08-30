package br.com.afya.triagemsocial

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import br.com.afya.triagemsocial.ui.theme.TriagemSocialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriagemSocialTheme {

                TriagemScreen()

            }
        }
    }
}

@Composable
fun TriagemScreen() {

    //Estado dos campos
    var nome by rememberSaveable {
        mutableStateOf("")
    }

    var idade by rememberSaveable {
        mutableStateOf("")
    }

    var especialidade by rememberSaveable {
        mutableStateOf("")
    }

    var resultado by rememberSaveable {
        mutableStateOf("")
    }

    FormularioTriagem(
        nome = nome,
        idade = idade,
        especialidade = especialidade,
        resultado = resultado,

        onNomeChange = {
            nome = it
        },

        onIdadeChange = {
            idade = it
        },

        onEspecialidadeChange = {
            especialidade = it
        },

        onTriagemClick = {

            resultado = if (
                nome.isBlank() ||
                idade.isBlank() ||
                especialidade.isBlank()
            ) {

                "Preencha todos os campos antes de continuar."

            } else {

                "Triagem realizada. Atendimento indicado: $especialidade"
            }
        }
    )
}

@Composable
fun FormularioTriagem(
    nome: String,
    idade: String,
    especialidade: String,
    resultado: String,
    onNomeChange: (String) -> Unit,
    onIdadeChange: (String) -> Unit,
    onEspecialidadeChange: (String) -> Unit,
    onTriagemClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        // Título principal
        Text(
            text = "Triagem Social",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Atendimento Social Interdisciplinar",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Informe seus dados para iniciar o atendimento.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // Card com os dados pessoais
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
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

                Spacer(
                    modifier = Modifier.height(16.dp)
                )


                // Campo Nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = onNomeChange,
                    label = {
                        Text("Nome")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )


                // Campo Idade
                OutlinedTextField(
                    value = idade,
                    onValueChange = onIdadeChange,
                    label = {
                        Text("Idade")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // Card com as áreas
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Área de atendimento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Selecione uma das áreas disponíveis:"
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )


                // Primeira linha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = {
                            onEspecialidadeChange("Psicologia")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Psicologia")
                    }

                    Button(
                        onClick = {
                            onEspecialidadeChange("Direito")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Direito")
                    }
                }


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                // Segunda linha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = {
                            onEspecialidadeChange("Fisioterapia")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Fisioterapia")
                    }

                    Button(
                        onClick = {
                            onEspecialidadeChange("Farmácia")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Farmácia")
                    }
                }


                // Só aparece quando uma especialidade for escolhida
                if (especialidade.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "Área selecionada: $especialidade",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // Botão principal
        Button(
            onClick = onTriagemClick,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Realizar Triagem"
            )
        }


        // Resultado
        if (resultado.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Resultado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = resultado,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}