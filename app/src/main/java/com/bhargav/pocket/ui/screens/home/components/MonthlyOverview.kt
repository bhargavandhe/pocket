package com.bhargav.pocket.ui.screens.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bhargav.pocket.commons.utils.getDate
import com.bhargav.pocket.commons.utils.innerPadding
import com.bhargav.pocket.commons.utils.toCurrency
import com.bhargav.pocket.model.Categories
import com.bhargav.pocket.model.Category
import com.bhargav.pocket.model.User
import com.bhargav.pocket.model.stringCategoryMapping
import java.util.*

private const val TAG = "MonthlyOverview"

@Composable
fun MonthlyOverview(userData: User, calendar: Calendar) {
    var count by remember { mutableStateOf(3) }
    var toggle by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 200,
                )
            )
    ) {
        val categories = userData.spendings.filter { it.value.title != Categories.MONEY_IN.name }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = innerPadding, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = getDate(date = calendar.timeInMillis, pattern = "MMM, YYYY"),
                style = MaterialTheme.typography.h5
            )
            if (categories.values.filter { it.total > 0 }.size > 3)
                Text(
                    text = "show " + if (toggle) "less" else "more",
                    modifier = Modifier.clickable {
                        count = if (toggle) 3 else categories.size
                        toggle = !toggle
                    }
                )
        }
        var total = 0f
        categories.forEach { total += it.value.total }

        for (category in categories.values.filter { it.total > 0 }.sortedByDescending { it.total }.take(count)) {
            CategoryOverview(
                category = stringCategoryMapping[category.title]!!,
                amount = category.total,
                monthlyTotal = total
            )
        }
    }
}

@Composable
fun CategoryOverview(category: Category, amount: Float, monthlyTotal: Float) {

    var animationPlayed by remember { mutableStateOf(false) }
    val max = amount / monthlyTotal

    val anim = animateFloatAsState(
        targetValue = if (animationPlayed) max else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 100,
        )
    )

    LaunchedEffect(key1 = true) { animationPlayed = true }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    color = category.color,
                    size = Size(width = size.width * anim.value, height = size.height)
                )
            }
            .padding(vertical = 8.dp, horizontal = innerPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(painter = painterResource(id = category.icon), contentDescription = "icon")
            Column {
                Text(text = category.title, style = MaterialTheme.typography.h6)
                Text(text = toCurrency(amount), style = MaterialTheme.typography.body2)
            }
        }
        Text(text = String.format("%.1f", anim.value * 100) + "%", style = MaterialTheme.typography.h6)
    }
}
