package br.com.afya.triagemsocial.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AfyaPink,
    onPrimary = Color.White,

    primaryContainer = AfyaPinkDark,
    onPrimaryContainer = Color.White,

    background = AfyaDarkBackground,
    onBackground = AfyaDarkText,

    surface = AfyaDarkSurface,
    onSurface = AfyaDarkText,

    surfaceVariant = AfyaDarkSurface,
    onSurfaceVariant = AfyaDarkTextSecondary,

    outline = AfyaDarkBorder,

    error = StatusError
)

private val LightColorScheme = lightColorScheme(
    primary = AfyaPink,
    onPrimary = Color.White,

    primaryContainer = AfyaPinkLight,
    onPrimaryContainer = AfyaPinkDark,

    background = AfyaLightBackground,
    onBackground = AfyaLightText,

    surface = AfyaLightSurface,
    onSurface = AfyaLightText,

    surfaceVariant = AfyaLightSurface,
    onSurfaceVariant = AfyaLightTextSecondary,

    outline = AfyaLightBorder,

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