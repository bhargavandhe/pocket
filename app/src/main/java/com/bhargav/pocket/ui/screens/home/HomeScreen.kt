package com.bhargav.pocket.ui.screens.home

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bhargav.pocket.commons.utils.innerPadding
import com.bhargav.pocket.model.User
import com.bhargav.pocket.ui.screens.home.components.Header
import com.bhargav.pocket.ui.screens.home.components.MonthlyOverview
import com.bhargav.pocket.ui.screens.home.components.RecentTransactions
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

private const val TAG = "HomeScreen"

@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(navController: NavController, paddingValues: PaddingValues) {
    val viewModel: HomeScreenViewModel = viewModel()

    val userData = viewModel.userData.observeAsState(initial = User())
    val transactions = viewModel.transactions.observeAsState(initial = listOf())
    val calendarInstance = Calendar.getInstance()

    LaunchedEffect(key1 = userData.value.lastReset != 0L) {
        calendarInstance.timeInMillis = userData.value.lastReset
        if (userData.value.lastReset != 0L &&
            calendarInstance.get(Calendar.MONTH) < Calendar.getInstance().get(Calendar.MONTH)
        ) viewModel.performReset()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(state = rememberScrollState()),
    ) {
        Header(userData = userData.value, navController = navController)

        MonthlyOverview(userData = userData.value, calendar = calendarInstance)

        RecentTransactions(navController = navController, transactions = transactions.value)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp)
                .padding(innerPadding)
                .background(color = Color(0x1AFFFFFF), shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Ads here")
        }
    }
}
