package com.bhargav.pocket.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhargav.pocket.model.Categories
import com.bhargav.pocket.model.CategoryData
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

const val USERS: String = "users"
const val PAYMENT_METHOD: String = "paymentMethod"
const val CASH: String = "cash"
const val TRANSACTIONS: String = "transactions"
const val TOTAL: String = "total"

const val NAME = "name"
const val EMAIL = "email"
const val NETBALANCE = "netBalance"
const val MONTHLY_INCOME = "monthlyIncome"
const val MONTHLY_EXPENSE = "monthlyExpense"
const val SPENDINGS = "spendings"
const val LAST_RESET = "lastReset"
const val BALANCE = "balance"
const val DATE = "date"
const val TRANSACTION_COUNT = "transactionCount"
const val SAVED_TITLES = "savedTitles"
const val TAG = "Repository"

@ExperimentalCoroutinesApi
class Repository {
    companion object {
        private var repository: Repository? = null
        fun getInstance(): Repository {
            if (repository == null) {
                synchronized(Repository::class.java) {
                    if (repository == null) {
                        repository = Repository()
                    }
                }
            }
            return repository!!
        }
    }

    private val auth = FirebaseAuth.getInstance()
    var uid = auth.currentUser?.uid
    private val firestore = FirebaseFirestore.getInstance()
    var userRef: DocumentReference? = null

    val userData: MutableLiveData<User> = MutableLiveData()

    fun init() {
        uid = auth.currentUser?.uid
        userRef = firestore.collection(USERS).document(uid!!)
        getUserData()
    }

    private fun getUserData() {
        userRef?.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                if (value.data == null) setNewUser(
                    email = auth.currentUser?.email!!,
                    name = "User",
                    balance = 0f
                )
                userData.value = if (value.data != null) value.toObject(User::class.java) else User(
                    email = auth.currentUser?.email!!,
                )
                Log.d(TAG, "getUserData: ${value.data}")
            }
        }
    }

    init {
        if (auth.currentUser != null) init()
    }

    fun getTransactions(limit: Long): Flow<List<Transaction>> = callbackFlow {
        val listener = userRef?.collection(TRANSACTIONS)?.orderBy(DATE, Query.Direction.DESCENDING)
            ?.limit(limit)?.addSnapshotListener { value, error ->
                if (error == null && value != null) trySend(value.toObjects())
            }
        awaitClose { listener?.remove() }
    }

    fun addTransaction(transaction: Transaction, docRef: DocumentReference = userRef!!) =
        docRef.collection(TRANSACTIONS).document(transaction.transactionId).set(transaction)

    fun deleteTransaction(transactionId: String): Task<Void> =
        userRef!!.collection(TRANSACTIONS).document(transactionId).delete()

    fun setNewUser(email: String, name: String, balance: Float) {

        val spendings = mutableMapOf<String, CategoryData>()

        for (c in Categories.values()) spendings[c.name] = CategoryData(title = c.name)
        Log.d(TAG, "setNewUser: $spendings")

        firestore.collection(USERS).document(auth.currentUser?.uid!!).set(
            mapOf(
                NAME to name,
                EMAIL to email,
                LAST_RESET to Calendar.getInstance().timeInMillis,
                NETBALANCE to balance,
                MONTHLY_INCOME to balance,
                MONTHLY_EXPENSE to 0f,
                SPENDINGS to spendings,
                TRANSACTION_COUNT to 0,
                SAVED_TITLES to mapOf("money in" to 1, "money out" to 1)
            )
        )
    }

    fun updateProfile(data: Map<String, Any>) = userRef?.update(data)

    fun getTransaction(docId: String) = callbackFlow {
        userRef!!.collection(TRANSACTIONS).document(docId).get().addOnSuccessListener {
            trySend(it.toObject(Transaction::class.java))
        }
        awaitClose { }
    }

    fun editTransaction(transaction: Transaction) =
        userRef!!.collection(TRANSACTIONS).document(transaction.transactionId).set(transaction)

    fun deleteCollection(ref: DocumentReference? = userRef): Task<Void>? = ref?.delete()
}
