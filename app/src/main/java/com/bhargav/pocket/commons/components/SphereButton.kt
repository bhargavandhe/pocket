package com.bhargav.pocket.commons.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SphereButton(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    color: Color = MaterialTheme.colors.primary,
    enabled: Boolean = true,
    onClick: () -> Unit = { }
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        elevation = null,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        onClick = { onClick.invoke() },
        content = {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.button,
                color = textColor
            )
        }
    )
}
