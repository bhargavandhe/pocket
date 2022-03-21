package com.bhargav.pocket.commons.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bhargav.pocket.commons.utils.getDate
import com.bhargav.pocket.commons.utils.getTime
import com.bhargav.pocket.commons.utils.toCurrency
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.model.TransactionType
import com.bhargav.pocket.model.categoryMapping

private const val TAG = "TransactionListItem"

@ExperimentalMaterialApi
@Composable
fun TransactionListItem(
    transaction: Transaction,
    onEdit: () -> Unit,
) {
    val isDebit = transaction.transactionType == TransactionType.DEBIT
    val category = categoryMapping[transaction.category]!!

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable { onEdit() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background)
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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
                    Text(text = transaction.title, style = MaterialTheme.typography.h6)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Chip(onClick = { }) {
                            Text(text = category.emoji + " " + category.title, style = MaterialTheme.typography.caption)
                        }
                        Chip(onClick = { }) {
                            Text(text = "🗓 " + getDate(transaction.date), style = MaterialTheme.typography.caption)
                        }
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = toCurrency(transaction.amount),
                    style = MaterialTheme.typography.h6,
                    color = if (isDebit) Color.Red else Color.Green
                )
                Text(
                    text = getTime(transaction.date),
                    style = MaterialTheme.typography.overline,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}
