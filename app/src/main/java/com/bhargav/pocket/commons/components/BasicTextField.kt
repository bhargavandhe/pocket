package com.bhargav.pocket.commons.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SphereBasicTextField(
    modifier: Modifier = Modifier,
    value: String,
    textSize: TextUnit = 18.sp,
    placeholder: String = "",
    placeholderOpacity: Float = 0.5f,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) = BasicTextField(
    modifier = modifier,
    value = value,
    onValueChange = { onValueChange(it) },
    textStyle = LocalTextStyle.current.copy(
        color = MaterialTheme.colors.onSurface,
        fontSize = textSize
    ),
    cursorBrush = SolidColor(MaterialTheme.colors.primary),
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    decorationBox = { innerTextField ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) Text(
                    text = placeholder,
                    style = LocalTextStyle.current.copy(
                        color = MaterialTheme.colors.onSurface.copy(alpha = placeholderOpacity),
                        fontSize = textSize
                    )
                )
                innerTextField()
            }
        }
    }
)
