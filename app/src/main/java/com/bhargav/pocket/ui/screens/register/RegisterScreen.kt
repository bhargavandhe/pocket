package com.bhargav.pocket.ui.screens.register

import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bhargav.pocket.Routes
import com.bhargav.pocket.commons.components.SphereButton
import com.bhargav.pocket.commons.components.SphereLogo
import com.bhargav.pocket.commons.components.SphereTextField
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun RegisterScreen(navController: NavController, navArgs: Bundle?) {

    val viewModel: RegisterScreenViewModel = viewModel()
    val decoded = Uri.decode(navArgs?.getString("credentials"))
    val credentials = Gson().fromJson<Map<String, String>>(decoded, Map::class.java)

    val email = credentials["email"]!!
    val password = credentials["password"]!!

    var name by remember { mutableStateOf("") }

    var balance by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
        ) {
            SphereLogo()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = buildAnnotatedString {
                    append("Let's get\n")
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                        append("you")
                    }
                    append(" started.")
                },
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enter your name and set your payment methods.",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.secondary
            )

            Spacer(modifier = Modifier.height(64.dp))

//            PaymentCard(paymentMethod = paymentMethod)

            SphereTextField(
                label = "Name",
                placeholder = "Enter your full name",
                value = name,
                onValueChanged = { name = it }
            )

            SphereTextField(
                label = "Balance",
                placeholder = "Enter your starting balance",
                value = balance,
                onValueChanged = { balance = it }
            )
        }

        SphereButton(
            modifier = Modifier.navigationBarsWithImePadding(),
            text = "register",
            enabled = name.isNotEmpty() && balance.toFloatOrNull() != null,
            onClick = {
                viewModel.createNewUser(
                    email = email,
                    password = password,
                    name = name,
                    balance = balance.toFloatOrNull() ?: 0f
                )
                navController.navigate(Routes.Home.route)
            }
        )
    }

}
