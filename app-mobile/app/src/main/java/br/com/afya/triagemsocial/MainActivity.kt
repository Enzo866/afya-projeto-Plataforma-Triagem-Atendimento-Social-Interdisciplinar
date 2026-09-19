package br.com.afya.triagemsocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.afya.triagemsocial.navigation.AppNavigation
import br.com.afya.triagemsocial.ui.theme.TriagemSocialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            TriagemSocialTheme {

                AppNavigation()
            }
        }
    }
}