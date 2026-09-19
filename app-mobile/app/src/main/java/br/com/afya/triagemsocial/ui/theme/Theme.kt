package br.com.afya.triagemsocial.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(

    // Cor principal Afya
    primary = AfyaPink,
    onPrimary = Color.White,

    // Destaques principais
    primaryContainer = AfyaPinkDark,
    onPrimaryContainer = Color.White,

    // Cards destacados
    secondaryContainer = AfyaDarkCard,
    onSecondaryContainer = AfyaDarkText,

    // Fundo
    background = AfyaDarkBackground,
    onBackground = AfyaDarkText,

    // Superfícies
    surface = AfyaDarkSurface,
    onSurface = AfyaDarkText,

    surfaceVariant = AfyaDarkSurfaceVariant,
    onSurfaceVariant = AfyaDarkTextSecondary,

    // Bordas
    outline = AfyaDarkBorder,

    // Erros
    error = StatusError
)

private val LightColorScheme = lightColorScheme(

    // Cor principal Afya
    primary = AfyaPink,
    onPrimary = Color.White,

    // Destaques principais
    primaryContainer = AfyaPinkLight,
    onPrimaryContainer = AfyaPinkDark,

    // Cards destacados
    secondaryContainer = AfyaLightCard,
    onSecondaryContainer = AfyaLightText,

    // Fundo
    background = AfyaLightBackground,
    onBackground = AfyaLightText,

    // Superfícies
    surface = AfyaLightSurface,
    onSurface = AfyaLightText,

    surfaceVariant = AfyaLightSurfaceVariant,
    onSurfaceVariant = AfyaLightTextSecondary,

    // Bordas
    outline = AfyaLightBorder,

    // Erros
    error = StatusError
)

@Composable
fun TriagemSocialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}