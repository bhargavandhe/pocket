package com.bhargav.pocket.ui.screens.onBoading

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bhargav.pocket.Routes
import com.bhargav.pocket.commons.components.SphereButton

@Composable
fun OnBoarding(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = buildAnnotatedString {
                append("Simple way to\nhelp control your\n")
                withStyle(
                    style = SpanStyle(color = MaterialTheme.colors.primary)
                ) { append("savings.") }
            },
            style = MaterialTheme.typography.h4
        )

        Spacer(modifier = Modifier.height(28.dp))
        SphereButton(
            text = "Proceed",
            onClick = { navController.navigate(Routes.Login.route) }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
