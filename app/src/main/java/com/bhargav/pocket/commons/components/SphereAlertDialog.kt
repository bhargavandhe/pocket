package com.bhargav.pocket.commons.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun SphereAlertDialog(
    openState: Boolean = false,
    title: String,
    text: String,
    buttons: @Composable () -> Unit,
    onDismiss: () -> Unit
) {
    if (openState)
        AlertDialog(
            onDismissRequest = { onDismiss.invoke() },
            title = { Text(text = title) },
            text = { Text(text = text) },
            buttons = { buttons() }
        )
}
