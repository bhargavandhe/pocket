package com.bhargav.pocket.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bhargav.pocket.Routes
import com.bhargav.pocket.commons.components.TransactionListItem
import com.bhargav.pocket.commons.utils.innerPadding
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.repository.DATE
import com.bhargav.pocket.repository.Repository
import com.bhargav.pocket.repository.TRANSACTIONS
import com.bhargav.pocket.ui.theme.typography
import com.google.firebase.firestore.Query
import com.jet.firestore.JetFirestore
import com.vanpra.composematerialdialogs.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

private const val TAG = "HistoryScreen"

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun HistoryScreen(navController: NavController, paddingValues: PaddingValues) {
    var transactions by remember { mutableStateOf(listOf<Transaction>()) }
    var sortParam by remember { mutableStateOf(DATE) }
    var direction by remember { mutableStateOf(Query.Direction.DESCENDING) }
    var searchQuery by remember { mutableStateOf("") }

    val dialogState = rememberMaterialDialogState()

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("ok".uppercase())
            negativeButton("cancel".uppercase())
        }
    ) {
        title("Sort by")
        val options = listOf("title", "amount", "date", "category")
        listItemsSingleChoice(
            list = options.map { it.capitalize() },
            initialSelection = options.indexOf(sortParam)
        ) {
            sortParam = when (it) {
                0 -> "title"
                1 -> "amount"
                3 -> "category"
                else -> "date"
            }
            transactions = listOf()
        }

        message("Direction")

        listItemsSingleChoice(
            list = listOf("Ascending", "Descending"),
            initialSelection = if (direction == Query.Direction.ASCENDING) 0 else 1
        ) {
            direction = when (it) {
                0 -> Query.Direction.ASCENDING
                else -> Query.Direction.DESCENDING
            }
        }
    }

    JetFirestore(
        path = { Repository.getInstance().userRef?.collection(TRANSACTIONS)!! },
        queryOnCollection = { orderBy(sortParam, direction) },
        limitOnSingleTimeCollectionFetch = 10,
        onSingleTimeCollectionFetch = { values, _ ->
            transactions = transactions + values?.toObjects(Transaction::class.java)!!
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                content = { Text(text = "History", style = typography.h4) }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = innerPadding)
                    .padding(bottom = 16.dp),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(text = "Search") },
                trailingIcon = {
                    IconButton(onClick = { dialogState.show() }) {
                        Icon(imageVector = Icons.Rounded.FilterList, contentDescription = "sort")
                    }
                }
            )

            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(items = transactions.toList()) { index, transaction ->
                    TransactionListItem(
                        transaction = transaction,
                        onEdit = { navController.navigate(Routes.EditTransaction.passUUID(transaction.transactionId)) },
                    )
                    if (index < transactions.size - 1) Divider()
                }

                if (transactions.size >= 10) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            content = {
                                TextButton(
                                    onClick = { it.loadNextPage() },
                                    content = { Text(text = "SHOW MORE") }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
