package com.bhargav.pocket.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bhargav.pocket.commons.components.ProfileIcon
import com.bhargav.pocket.commons.components.SphereButton
import com.bhargav.pocket.commons.components.SphereTextField
import com.bhargav.pocket.commons.components.SphereTopAppBar
import com.bhargav.pocket.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun EditProfileScreen(navController: NavController) {
    val viewModel: EditProfileViewModel = viewModel()
    val userData = viewModel.userData.observeAsState(initial = User())

    var name by remember { mutableStateOf(userData.value.name) }

    LaunchedEffect(
        key1 = userData.value != null,
        block = { name = userData.value.name }
    )

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (appBar, layout, submit) = createRefs()

        SphereTopAppBar(
            modifier = Modifier.constrainAs(appBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            title = "Edit profile",
            navigationIcon = Icons.Default.ArrowBack,
            onNavigationClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .constrainAs(layout) {
                    top.linkTo(appBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(96.dp))
            ProfileIcon(modifier = Modifier.size(112.dp))
            Spacer(modifier = Modifier.height(48.dp))
            SphereTextField(
                label = "Name",
                value = name,
                onValueChanged = { name = it }
            )
        }

        SphereButton(
            modifier = Modifier
                .constrainAs(submit) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(24.dp),
            text = "save changes"
        ) {
            if (userData.value?.name != name)
                viewModel.editName(name = name)?.addOnSuccessListener { navController.popBackStack() }
            navController.popBackStack()
        }
    }
}

private const val TAG = "EditProfile"
