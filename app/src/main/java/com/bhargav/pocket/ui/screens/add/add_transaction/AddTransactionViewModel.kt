package com.bhargav.pocket.ui.screens.add.add_transaction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.model.TransactionType
import com.bhargav.pocket.model.User
import com.bhargav.pocket.repository.*
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.math.absoluteValue

@ExperimentalCoroutinesApi
class AddTransactionViewModel : ViewModel() {
    private val repository: Repository = Repository.getInstance()
    val userData: LiveData<User> = repository.userData

    private val _time = MutableLiveData(Calendar.getInstance().timeInMillis)
    var time: LiveData<Long> = _time

    fun selectDateTime(context: Context) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                _time.value = pickedDateTime.timeInMillis
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    fun addTransaction(transaction: Transaction) {
        repository.addTransaction(transaction).addOnSuccessListener {
            val sign = if (transaction.transactionType == TransactionType.DEBIT) -1 else 1
            val delta: Double = (sign * transaction.amount).toDouble()

            repository.updateProfile(
                data = mapOf(
                    NETBALANCE to FieldValue.increment(delta),
                    TRANSACTION_COUNT to FieldValue.increment(1),
                    (if (transaction.transactionType == TransactionType.DEBIT) MONTHLY_EXPENSE else MONTHLY_INCOME)
                            to FieldValue.increment(delta.absoluteValue),
                    "$SPENDINGS.${transaction.category.name}.total" to FieldValue.increment(delta.absoluteValue),
                    "$SPENDINGS.${transaction.category.name}.transactions" to FieldValue.arrayUnion(transaction.transactionId),
                    "$SAVED_TITLES.${transaction.title.trim().lowercase()}" to FieldValue.increment(1)
                )
            )
        }
    }
}
