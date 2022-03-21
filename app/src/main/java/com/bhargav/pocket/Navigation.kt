package com.bhargav.pocket


import android.net.Uri
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.bhargav.pocket.ui.screens.add.add_transaction.AddTransactionScreen
import com.bhargav.pocket.ui.screens.edit.edit_transaction.EditTransaction
import com.bhargav.pocket.ui.screens.history.HistoryScreen
import com.bhargav.pocket.ui.screens.home.HomeScreen
import com.bhargav.pocket.ui.screens.login.LoginScreen
import com.bhargav.pocket.ui.screens.onBoading.OnBoarding
import com.bhargav.pocket.ui.screens.register.RegisterScreen
import com.bhargav.pocket.ui.screens.settings.SettingsScreen
import com.bhargav.pocket.ui.screens.settings.EditProfileScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi

enum class Tabs(val title: String, val icon: ImageVector, val route: String) {
    HOME(title = "Home", icon = Icons.Default.Home, route = Routes.Home.route),

    //    ANALYSIS(title = "Analytics", icon = Icons.Default.StackedLineChart, route = Routes.Analytics.route),
    HISTORY(title = "History", icon = Icons.Default.SwapHoriz, route = Routes.History.route),
}

sealed class Routes(open val route: String) {
    object OnBoarding : Routes("on-boarding")
    object Login : Routes("login")
    object Register : Routes("register/{credentials}") {
        fun passEmailAndPassword(email: String, password: String) =
            "register/${Uri.encode(Gson().toJson(mapOf("email" to email, "password" to password)))}"
    }

    object Settings : Routes("settings")
    object Home : Routes("home")
    object History : Routes("history")

    object AddTransaction : Routes("add-transactions")
    object AddPaymentMethod : Routes("add-payment-method")

    object EditTransaction : Routes("edit-transaction/{uuid}") {
        fun passUUID(uuid: String): String = "edit-transaction/$uuid"
    }

    object EditPaymentMethod : Routes("edit-paymentMethod/{paymentMethodId}") {
        fun passCardDetails(paymentMethodId: String) = "edit-paymentMethod/$paymentMethodId"
    }

    object EditProfile : Routes("edit-profile")
    object ManagePaymentMethods : Routes("manage-payment-methods")
}

@ExperimentalAnimationApi
val AnimatedContentScope<NavBackStackEntry>.enterTransition
    get() = slideIntoContainer(
        AnimatedContentScope.SlideDirection.Left,
        animationSpec = tween(300)
    )


@ExperimentalAnimationApi
val AnimatedContentScope<NavBackStackEntry>.exitTransition
    get() = slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Left,
        animationSpec = tween(300)
    )

@ExperimentalAnimationApi
val AnimatedContentScope<NavBackStackEntry>.popEnter
    get() = slideIntoContainer(
        AnimatedContentScope.SlideDirection.Right,
        animationSpec = tween(300)
    )

@ExperimentalAnimationApi
val AnimatedContentScope<NavBackStackEntry>.popExit
    get() = slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Right,
        animationSpec = tween(300)
    )


@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun Navigation(navController: NavHostController, paddingValues: PaddingValues) {

    var startDestination = Routes.OnBoarding.route
    if (FirebaseAuth.getInstance().currentUser != null) startDestination = Routes.Home.route

    AnimatedNavHost(
        navController = navController, startDestination = startDestination,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
        popEnterTransition = { popEnter },
        popExitTransition = { popExit },
    ) {

        // boarding screen
        composable(route = Routes.OnBoarding.route, content = { OnBoarding(navController = navController) })

        // login and register
        composable(
            route = Routes.Login.route,
            content = { LoginScreen(navController = navController) }
        )
        composable(
            route = Routes.Register.route,
            arguments = listOf(
                navArgument("credentials") {
                    type = NavType.StringType
                }
            ),
            content = { RegisterScreen(navController = navController, navArgs = it.arguments) }
        )

        // dashboard
        composable(
            route = Routes.Home.route,
            content = { HomeScreen(navController = navController, paddingValues = paddingValues) },
        )
        composable(
            route = Routes.History.route,
            content = { HistoryScreen(navController = navController, paddingValues = paddingValues) },
        )

        // add
        composable(
            route = Routes.AddTransaction.route,
            content = { AddTransactionScreen(navController = navController) }
        )

        // edit
//        composable(
//            route = Routes.ManagePaymentMethods.route,
//            content = { ManagePaymentMethods(navController = navController) }
//        )
        composable(
            route = Routes.EditTransaction.route,
            arguments = listOf(
                navArgument("uuid") {
                    type = NavType.StringType
                }
            ),
            content = { EditTransaction(navController = navController, navArgs = it.arguments) }
        )


        // settings
        composable(
            route = Routes.Settings.route,
            content = { SettingsScreen(navController = navController) }
        )
        composable(
            route = Routes.EditProfile.route,
            content = { EditProfileScreen(navController = navController) }
        )
    }
}
