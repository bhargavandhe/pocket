package com.bhargav.pocket.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = SpherePurple,
    background = Color.Black,
    secondary = SphereDarkSecondaryText,
    secondaryVariant = SphereGray700,
)

private val LightColorPalette = lightColors(
    primary = SpherePurple,
    background = Color.White,
    secondary = SphereLightSecondaryText,
    secondaryVariant = SphereGray700
)


@Composable
fun SphereTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = DarkColorPalette,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}
