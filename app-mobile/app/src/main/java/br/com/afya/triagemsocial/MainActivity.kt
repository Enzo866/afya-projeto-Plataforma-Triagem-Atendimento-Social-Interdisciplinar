package br.com.afya.triagemsocial

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ){
        //Título
        Text(
            text = "Triagem Social Interdisciplinar",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        //Campos
        //Nome
        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
            },
            label = {
                Text("Nome")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        //Idade
        OutlinedTextField(
            value = idade,
            onValueChange = {
                idade = it
            },
            label = {
                Text("Idade")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        //Título da Seleção
        Text(
            text = "Selecione a área de atendimento",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        //Linhas de especialidades
        //Primeira Linha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    especialidade = "Psicologia"
                }
            ) {
                Text("Psicologia")
            }

            Button(
                onClick = {
                    especialidade = "Direito"
                }
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
                    especialidade = "Fisioterapia"
                }
            ) {
                Text("Fisioterapia")
            }

            Button(
                onClick = {
                    especialidade = "Farmácia"
                }
            ) {
                Text("Farmácia")
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        //Especialidade Selecionada
        if (especialidade.isNotEmpty()){
            Text(
                text = "Área selecionada = $especialidade",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}