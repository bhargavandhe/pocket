package com.bhargav.pocket.ui.screens.edit.edit_transaction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.model.TransactionType
import com.bhargav.pocket.repository.*
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.math.absoluteValue

private const val TAG = "EditTransactionViewMode"

@ExperimentalCoroutinesApi
class EditTransactionViewModel : ViewModel() {
    private val repository = Repository.getInstance()
    val userData = repository.userData

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
                setTime(pickedDateTime.timeInMillis)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    fun setTime(time: Long) {
        _time.value = time
    }

    fun getTransactionDetails(uuid: String) = repository.getTransaction(docId = uuid)

    // TODO: Fix edit transaction
    fun editTransaction(initTransaction: Transaction, transaction: Transaction) =
        repository.editTransaction(transaction = transaction).addOnCompleteListener {
            val initSign = if (initTransaction.transactionType == TransactionType.DEBIT) 1 else -1
            val sign = if (transaction.transactionType == TransactionType.DEBIT) 1 else -1

            val data = mutableMapOf<String, FieldValue>()
            data[NETBALANCE] = FieldValue.increment(
                (initSign * initTransaction.amount - transaction.amount * sign).toDouble()
            )

            if (transaction.category != initTransaction.category) {
                data[listOf(SPENDINGS, initTransaction.category.name, TRANSACTIONS)
                    .joinToString(".")] = FieldValue.arrayRemove(initTransaction.transactionId)
                data[listOf(SPENDINGS, initTransaction.category.name, TOTAL)
                    .joinToString(".")] = FieldValue.increment(-initTransaction.amount.absoluteValue.toDouble())
                data[listOf(SPENDINGS, transaction.category.name, TRANSACTIONS)
                    .joinToString(".")] = FieldValue.arrayUnion(transaction.transactionId)
                data[listOf(SPENDINGS, transaction.category.name, TOTAL)
                    .joinToString(".")] = FieldValue.increment(transaction.amount.absoluteValue.toDouble())
            }
            repository.updateProfile(data)
        }

    fun deleteTransaction(transaction: Transaction) =
        repository.deleteTransaction(transaction.transactionId).addOnSuccessListener {
            val sign = if (transaction.transactionType == TransactionType.DEBIT) 1 else -1
            val delta: Double = (sign * transaction.amount).toDouble()

            repository.updateProfile(
                mapOf(
                    NETBALANCE to FieldValue.increment(delta),
                    TRANSACTION_COUNT to FieldValue.increment(-1),
                    (if (transaction.transactionType == TransactionType.DEBIT) MONTHLY_EXPENSE else MONTHLY_INCOME)
                            to FieldValue.increment(-delta.absoluteValue),

                    "$SPENDINGS.${transaction.category.name}.transactions" to FieldValue.arrayRemove
                        (transaction.transactionId),
                    "$SPENDINGS.${transaction.category.name}.total" to FieldValue.increment
                        ((-1 * transaction.amount.absoluteValue).toDouble())
                )
            )
        }
}
