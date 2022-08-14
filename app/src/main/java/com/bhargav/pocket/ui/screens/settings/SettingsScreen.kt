package com.bhargav.pocket.ui.screens.settings

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bhargav.pocket.Routes
import com.bhargav.pocket.commons.components.ProfileIcon
import com.bhargav.pocket.commons.components.SphereAlertDialog
import com.bhargav.pocket.commons.components.SphereTopAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class SettingsOptions(
    val title: String,
    val subtitle: String = "",
    val icon: ImageVector,
    val action: @Composable () -> Unit = { },
    val onClick: () -> Unit
)

data class SettingsSection(val title: String, val options: List<SettingsOptions>, val tint: Color)

private const val TAG = "SettingsScreen"

@ExperimentalCoroutinesApi
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = viewModel()

    val userData = viewModel.userData.observeAsState()
    var appLockSwitch by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }

    SphereAlertDialog(
        openState = openDialog.value,
        title = "Logout",
        text = "Are you sure, you want to logout?",
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 16.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        openDialog.value = false
                        navController.navigate(Routes.OnBoarding.route)
                    },
                    content = { Text("yes".uppercase()) }
                )

                TextButton(
                    onClick = { openDialog.value = false },
                    content = { Text("no".uppercase()) }
                )
            }
        },
        onDismiss = { openDialog.value = false }
    )
    val context = LocalContext.current

    val settingsSections = listOf(
        SettingsSection(
            title = "account",
            tint = Color(0xFF034676),
            options = listOf(
                SettingsOptions(
                    title = "Edit Profile",
                    icon = Icons.Default.AccountCircle,
                    action = {
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "go")
                    }, onClick = { navController.navigate(Routes.EditProfile.route) }
                ), SettingsOptions(
                    title = "Manage payment methods",
                    icon = Icons.Default.Payments,
                    action = {
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "go")
                    }, onClick = {
                        Toast.makeText(
                            context,
                            "Coming soon!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            )
        ), SettingsSection(
            title = "security",
            tint = Color(0xFF037608),
            options = listOf(
                SettingsOptions(
                    title = "Change password",
                    icon = Icons.Default.Password,
                    action = {
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "go")
                    },
                    onClick = {
                        Toast.makeText(
                            context,
                            "Coming soon!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ), SettingsOptions(
                    title = "Use app lock",
                    subtitle = "Currently turned " + if (appLockSwitch) "on" else "off",
                    icon = Icons.Default.Lock,
                    action = {
                        Switch(
                            checked = appLockSwitch,
                            onCheckedChange = { appLockSwitch = !appLockSwitch },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
                        )
                    },
                    onClick = {
                        appLockSwitch = !appLockSwitch
                        Log.d(TAG, "SettingsScreen: $appLockSwitch")
                    }
                )
            )
        ), SettingsSection(
            title = "app",
            tint = Color(0xFF765603),
            options = listOf(
                SettingsOptions(
                    title = "About us",
                    icon = Icons.Default.Info,
                    action = {
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "go")
                    },
                    onClick = { Log.d(TAG, "SettingsScreen: About us") }
                ), SettingsOptions(
                    title = "Help and Support",
                    icon = Icons.Default.HelpOutline,
                    action = {
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "go")
                    },
                    onClick = {
                        Toast.makeText(
                            context,
                            "Coming soon!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            )
        )
    )

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (appbar, content) = createRefs()
        SphereTopAppBar(
            modifier = Modifier.constrainAs(appbar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            },
            title = "Settings",
            navigationIcon = Icons.Default.NavigateBefore,
            onNavigationClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .constrainAs(content) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(appbar.bottom)
                }
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(state = rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Routes.EditProfile.route) }
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileIcon(
                        modifier = Modifier.size(72.dp),
                        onClick = { navController.navigate(Routes.EditProfile.route) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = userData.value?.name!!, style = MaterialTheme.typography.h5)
                        Text(text = userData.value?.email!!, style = MaterialTheme.typography.caption)
                    }
                }
            }


            settingsSections.map { settingsSection ->
                SettingsSectionItem(settingsSection)
            }

            SettingsOption(
                settingsOption = SettingsOptions(
                    title = "Logout",
                    icon = Icons.Default.Logout,
                    onClick = {
                        openDialog.value = true
                    }
                ),
                tint = Color(0xFF760303)
            )

            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun SettingsSectionItem(settingsSection: SettingsSection) {
    Text(
        modifier = Modifier.padding(start = 24.dp),
        text = settingsSection.title.uppercase(),
        style = MaterialTheme.typography.subtitle2
    )
    Spacer(modifier = Modifier.height(8.dp))
    Divider(
        color = Color.White.copy(alpha = 0.3f),
        thickness = 0.3.dp
    )
    Spacer(modifier = Modifier.height(4.dp))
    settingsSection.options.forEach {
        SettingsOption(
            settingsOption = it,
            tint = settingsSection.tint
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}


@Composable
fun SettingsOption(
    settingsOption: SettingsOptions,
    tint: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { settingsOption.onClick() }
            .padding(vertical = 8.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = CircleShape)
                    .background(color = tint),
                contentAlignment = Alignment.Center,
                content = {
                    Icon(
                        imageVector = settingsOption.icon,
                        contentDescription = settingsOption.title
                    )
                }
            )
            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = settingsOption.title, style = MaterialTheme.typography.subtitle1)
                if (settingsOption.subtitle != "")
                    Text(text = settingsOption.subtitle, style = MaterialTheme.typography.caption)
            }
        }
        settingsOption.action()
    }
}
