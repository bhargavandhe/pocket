package com.bhargav.pocket.ui.screens.edit.edit_transaction

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bhargav.pocket.R
import com.bhargav.pocket.commons.components.SphereAlertDialog
import com.bhargav.pocket.commons.components.SphereBasicTextField
import com.bhargav.pocket.commons.components.SphereTopAppBar
import com.bhargav.pocket.commons.components.TitleSuggestions
import com.bhargav.pocket.commons.utils.*
import com.bhargav.pocket.model.*
import com.bhargav.pocket.ui.screens.add.add_transaction.Something
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.ExperimentalCoroutinesApi

private const val TAG = "EditTransaction"

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
fun EditTransaction(navController: NavController, navArgs: Bundle?) {
    val viewModel: EditTransactionViewModel = viewModel()
    val userData = viewModel.userData.observeAsState(initial = User())
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }

    val uuid = navArgs?.getString("uuid")!!
    val initTransaction = viewModel.getTransactionDetails(uuid = uuid).collectAsState(initial = Transaction())

    val balance = userData.value.netBalance

    var transactionId by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    val time = viewModel.time.observeAsState()
    var category by remember { mutableStateOf(Categories.MONEY_IN) }
    var isDebit by remember { mutableStateOf(true) }
    var lastEdited by remember { mutableStateOf(0L) }

    LaunchedEffect(
        key1 = initTransaction.value,
        block = {
            Log.d(TAG, "EditTransaction: In")
            transactionId = initTransaction.value?.transactionId ?: randomId()
            title = initTransaction.value?.title ?: ""
            amount = initTransaction.value?.amount?.toString() ?: "0.0"
            details = initTransaction.value?.details ?: ""
            viewModel.setTime(initTransaction.value?.date ?: 0)
            category = initTransaction.value?.category ?: Categories.MONEY_IN
            isDebit = initTransaction.value?.transactionType == TransactionType.DEBIT
            lastEdited = initTransaction.value?.lastEdited ?: 0L
            viewModel.setTime(initTransaction.value?.date ?: 0L)
        }
    )

    SphereAlertDialog(
        openState = openDialog.value,
        title = "Delete",
        text = "Are you sure, you want to delete this transaction?",
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 16.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = {
                        viewModel.deleteTransaction(initTransaction.value!!)
                        openDialog.value = false
                        navController.popBackStack()
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

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (appBar, layout, bottom) = createRefs()

        SphereTopAppBar(
            modifier = Modifier.constrainAs(appBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            title = "Edit Transaction",
            navigationIcon = Icons.Rounded.ArrowBack,
            onNavigationClick = { navController.popBackStack() },
            action = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { openDialog.value = true }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "delete")
                    }
                    IconButton(
                        onClick = {
                            isDebit =
                                if (category != Categories.MISC) category != Categories.MONEY_IN else isDebit
                            if (title.isNotEmpty() && amount.toFloatOrNull() != null) {
                                viewModel.editTransaction(
                                    initTransaction = initTransaction.value ?: Transaction(),
                                    transaction = Transaction(
                                        transactionId = transactionId,
                                        title = title,
                                        amount = amount.toFloat(),
                                        transactionType = if (isDebit) TransactionType.DEBIT else TransactionType.CREDIT,
                                        date = viewModel.time.value ?: 0,
                                        category = category,
                                        details = details,
                                    ),
                                )
                                navController.popBackStack()
                            } else Toast.makeText(
                                context,
                                "Please fill out all the fields!",
                                Toast.LENGTH_SHORT
                            ).show();
                        },
                    ) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = "save")
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .constrainAs(layout) {
                    top.linkTo(appBar.bottom)
                    start.linkTo(parent.start)
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = innerPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val cat = categoryMapping[category] ?: Category()
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(color = cat.color.copy(alpha = 0.3f), shape = CircleShape)
                        .clip(shape = CircleShape),
                    contentAlignment = Alignment.Center,
                    content = {
                        Image(
                            painter = painterResource(id = cat.icon),
                            contentDescription = "icon"
                        )
                    }
                )

                Column {
                    SphereBasicTextField(
                        modifier = Modifier.padding(horizontal = innerPadding),
                        value = title,
                        onValueChange = { title = it },
                        placeholder = "Add title",
                        textSize = 24.sp
                    )
                    SphereBasicTextField(
                        modifier = Modifier.padding(horizontal = innerPadding),
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = "₹0.00",
                        textSize = 32.sp,
                        keyboardType = KeyboardType.Number,
                        placeholderOpacity = 0.8f,
                    )
                }
            }

            TitleSuggestions(
                onClick = { title = it },
                suggestions = userData.value.savedTitles.toList().sortedByDescending { it.second }.toMap().keys
            )

            Divider(Modifier.padding(vertical = 8.dp))

            Something(
                icon = Icons.Default.Notes,
                something = {
                    SphereBasicTextField(
                        value = details,
                        onValueChange = { details = it },
                        placeholder = "Add details"
                    )
                }
            )

            Something(
                icon = Icons.Default.Schedule,
                something = {
                    Text(text = "${getDate(time.value!!)} - ${getTime(time.value!!)}")
                },
                onClick = { viewModel.selectDateTime(context) }
            )

            Divider(Modifier.padding(vertical = 8.dp))

            Something(
                icon = Icons.Default.Category,
                something = {
                    Text(text = "Category")
                }
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = innerPadding),
                mainAxisSpacing = 8.dp
            ) {
                Categories.values().map {
                    val cat = categoryMapping[it] ?: Category()
                    Chip(
                        onClick = { category = it },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = if (category == it) cat.color else Color.White.copy(alpha = 0.1f)
                        ),
                        content = { Text(text = cat.emoji + " " + cat.title) }
                    )
                }
            }

            AnimatedVisibility(visible = category == Categories.MISC) {
                Column {
                    Divider(Modifier.padding(vertical = 8.dp))
                    Something(
                        icon = Icons.Default.SwapHoriz,
                        something = {
                            Text(text = "Transaction type")
                        }
                    )

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isDebit = false }
                            .padding(horizontal = innerPadding)
                    ) {
                        val (radioButton, label) = createRefs()
                        RadioButton(
                            selected = !isDebit,
                            onClick = { isDebit = false },
                            modifier = Modifier.constrainAs(radioButton) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                this.bottom.linkTo(parent.bottom)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Green,
                                unselectedColor = Color.Green
                            )
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(label) {
                                    start.linkTo(radioButton.end, margin = 12.dp)
                                    top.linkTo(parent.top)
                                    this.bottom.linkTo(parent.bottom)
                                },
                            text = "Credit"
                        )
                    }

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isDebit = true }
                            .padding(horizontal = innerPadding)
                    ) {
                        val (radioButton, label) = createRefs()
                        RadioButton(
                            selected = isDebit,
                            onClick = { isDebit = true },
                            modifier = Modifier.constrainAs(radioButton) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                this.bottom.linkTo(parent.bottom)
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Red, unselectedColor = Color.Red)
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(label) {
                                    start.linkTo(radioButton.end, margin = 12.dp)
                                    top.linkTo(parent.top)
                                    this.bottom.linkTo(parent.bottom)
                                },
                            text = "Debit"
                        )
                    }
                }
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = innerPadding)
                    .padding(vertical = 8.dp),
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.caption,
                text = "Last edited : ${getDate(lastEdited)}, ${getTime(lastEdited)}"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottom) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    this.bottom.linkTo(parent.bottom, margin = 16.dp)
                }
                .padding(horizontal = innerPadding)
                .border(width = 1.dp, color = Color(0x4DDADADA), shape = RoundedCornerShape(22))
                .clip(shape = RoundedCornerShape(22))
                .clickable { }
                .padding(horizontal = innerPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Image(
                    painter = painterResource(id = categoryMapping[category]?.icon ?: R.drawable.ic_transfer),
                    contentDescription = "type"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Cash",
                        style = MaterialTheme.typography.h5
                    )
                    Text(
                        text = toCurrency(balance),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = "selected")
        }
    }
}
