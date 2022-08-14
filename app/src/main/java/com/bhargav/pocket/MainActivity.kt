package com.bhargav.pocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bhargav.pocket.ui.theme.SphereTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FirebaseApp.initializeApp(this)
            SphereTheme {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    SphereTheme {
                        Surface(color = MaterialTheme.colors.background) {
                            Main()
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Main() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = Color.Black)

    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()

    val isValidScreen = listOf(
        Routes.Home.route,
        Routes.History.route
    ).contains(backStackEntry.value?.destination?.route)

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        floatingActionButton = {
            if (isValidScreen)
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.AddTransaction.route) },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    content = { Icon(imageVector = Icons.Rounded.Add, contentDescription = "add") }
                )
        },
        bottomBar = {
            if (isValidScreen) BottomNavigation {
                Tabs.values().forEach { tab ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null
                            )
                        },
                        label = { Text(text = tab.title) },
                        selected = tab.route == backStackEntry.value?.destination?.route,
                        onClick = {
                            if (tab.route != backStackEntry.value?.destination?.route)
                                navController.navigate(tab.route) { launchSingleTop = true }
                        },
                        alwaysShowLabel = false,
                        selectedContentColor = MaterialTheme.colors.primary,
                        unselectedContentColor = LocalContentColor.current,
                    )
                }
            }
        },
        content = {
            Navigation(navController = navController, paddingValues = it)
        }
    )
}
