package com.bhargav.pocket.ui.screens.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bhargav.pocket.Routes
import com.bhargav.pocket.commons.components.SphereButton
import com.bhargav.pocket.commons.components.SphereLogo
import com.bhargav.pocket.commons.components.SphereTextField
import com.bhargav.pocket.commons.utils.innerPadding
import com.bhargav.pocket.repository.Repository
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

private const val TAG = "LoginScreen"
private const val NO_SUCH_EMAIL =
    "There is no user record corresponding to this identifier. The user may have been deleted."
private const val INVALID_PASSWORD = "The password is invalid or the user does not have a password."

@ExperimentalCoroutinesApi
@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(all = innerPadding)
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
                    append("Enter your\n")
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) { append("email") }
                    append(" address\nto continue.")
                },
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We'll create a new account for you,\nif no existing account is found.",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.secondary
            )

            Spacer(modifier = Modifier.height(64.dp))

            SphereTextField(
                label = "Email",
                placeholder = "Enter your email address",
                value = viewModel.email,
                keyboardType = KeyboardType.Email,
                onValueChanged = { viewModel.email = it },
                gutterBottom = 24.dp,
                errorText = viewModel.emailError
            )

            SphereTextField(
                label = "Password",
                placeholder = "Enter your password",
                value = viewModel.password,
                keyboardType = KeyboardType.Password,
                onValueChanged = { viewModel.password = it },
                errorText = viewModel.passwordError
            )
        }

        SphereButton(
            modifier = Modifier.navigationBarsWithImePadding(),
            text = "proceed",
            onClick = {
                if (viewModel.email != "" && viewModel.password != "")
                    scope.launch {
                        val auth = FirebaseAuth.getInstance()
                        auth.signInWithEmailAndPassword(viewModel.email.trim(), viewModel.password)
                            .addOnSuccessListener {
                                Log.d(TAG, "LoginScreen: Success")
                                Repository.getInstance().init()
                                navController.navigate(Routes.Home.route)
                            }.addOnFailureListener {
                                when (it.message) {
                                    NO_SUCH_EMAIL -> navController.navigate(
                                        Routes.Register.passEmailAndPassword(
                                            email = viewModel.email,
                                            password = viewModel.password
                                        )
                                    )
                                    INVALID_PASSWORD -> viewModel.passwordError = "Invalid password!"
                                    else -> Log.d(TAG, "LoginScreen: ${it.message}")
                                }
                            }
                    }
            }
        )
    }
}
