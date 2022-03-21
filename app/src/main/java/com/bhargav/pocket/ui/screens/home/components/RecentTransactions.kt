package com.bhargav.pocket.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bhargav.pocket.Routes
import com.bhargav.pocket.commons.utils.getDate
import com.bhargav.pocket.commons.utils.getTime
import com.bhargav.pocket.commons.utils.toCurrency
import com.bhargav.pocket.model.Category
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.model.TransactionType
import com.bhargav.pocket.model.categoryMapping

@Composable
fun RecentTransactions(navController: NavController, transactions: List<Transaction>) {
    Text(
        modifier = Modifier.padding(start = 24.dp),
        text = "Recent Transactions",
        style = MaterialTheme.typography.h5
    )
    if (transactions.isNotEmpty())
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            transactions.mapIndexed { index, transaction ->
                val category: Category = categoryMapping[transaction.category]!!
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = MaterialTheme.shapes.medium)
                        .clickable { navController.navigate(Routes.EditTransaction.passUUID(transaction.transactionId)) }
                        .padding(all = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(color = category.color.copy(alpha = 0.3f), shape = CircleShape)
                                .clip(shape = CircleShape),
                            contentAlignment = Alignment.Center,
                            content = {
                                Image(
                                    painter = painterResource(id = category.icon),
                                    contentDescription = "icon"
                                )
                            }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = transaction.title,
                                style = MaterialTheme.typography.h6
                            )
                            Text(text = "🗓 " + getDate(transaction.date), style = MaterialTheme.typography.caption)
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = toCurrency(transaction.amount),
                            style = MaterialTheme.typography.h6,
                            color = if (transaction.transactionType == TransactionType.DEBIT) Color.Red else Color.Green
                        )
                        Text(
                            text = getTime(transaction.date),
                            style = MaterialTheme.typography.overline,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
                if (index < transactions.size - 1) Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = (0.5).dp
                )
            }
        }
    else Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(color = Color.White.copy(alpha = 0.1f), shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .height(100.dp),
        contentAlignment = Alignment.Center,
        content = {
            Text(
                textAlign = TextAlign.Center,
                text = "Nothing here! Make some\ntransactions and return.",
                style = MaterialTheme.typography.body1
            )
        }
    )
}
