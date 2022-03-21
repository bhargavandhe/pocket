package com.bhargav.pocket.ui.screens.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.model.TransactionType
import com.bhargav.pocket.model.User
import com.bhargav.pocket.repository.*
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.absoluteValue

@ExperimentalCoroutinesApi
class HistoryViewModel : ViewModel() {
    private val repository: Repository = Repository.getInstance()
    val userData: LiveData<User> = repository.userData

    fun deleteTransaction(transaction: Transaction) {
        repository.deleteTransaction(transactionId = transaction.transactionId).addOnSuccessListener {
            val sign = if (transaction.transactionType == TransactionType.DEBIT) 1 else -1
            val delta: Double = (sign * transaction.amount).toDouble()

            try {
                repository.updateProfile(
                    mapOf(
                        NETBALANCE to FieldValue.increment(delta),
                        TRANSACTION_COUNT to FieldValue.increment(-1),
                        "$PAYMENT_METHOD.$CASH.$BALANCE" to FieldValue.increment(delta),
                        (if (transaction.transactionType == TransactionType.DEBIT) MONTHLY_EXPENSE else MONTHLY_INCOME)
                                to FieldValue.increment(-delta.absoluteValue)
                    )
                )
            } catch (e: Exception) {
                Log.d(TAG, "deleteTransaction: $e")
            }
        }
    }
}

private const val TAG = "HistoryViewModel"
