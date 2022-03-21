package com.bhargav.pocket.commons.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bhargav.pocket.R

@Composable
fun SphereLogo(darkMode: Boolean = isSystemInDarkTheme()) =
    Image(
        painter = painterResource(id = if (darkMode) R.drawable.splash_light_logo else R.drawable.splash_dark_logo),
        contentDescription = "Logo",
        modifier = Modifier.width(100.dp)
    )
