package com.bhargav.pocket.ui.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bhargav.pocket.commons.components.SphereLogo
import com.bhargav.pocket.commons.utils.innerPadding

@Composable
fun AboutScreen() {
    SphereLogo()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Pocket for Android", style = MaterialTheme.typography.h5)
        Text(
            textAlign = TextAlign.Center,
            text = "Expenses tracker application for users.\nThe core feature of this app includes tracking daily transaction of an user."
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            textAlign = TextAlign.Center,
            text = "Developed by\nBhargav Andhe"
        )

        Text(text = "version 1.0")
    }
}


@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}