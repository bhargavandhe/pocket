package com.bhargav.pocket.ui.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.model.User
import com.bhargav.pocket.repository.LAST_RESET
import com.bhargav.pocket.repository.MONTHLY_EXPENSE
import com.bhargav.pocket.repository.MONTHLY_INCOME
import com.bhargav.pocket.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "HomeScreenViewModel"

@ExperimentalCoroutinesApi
class HomeScreenViewModel : ViewModel() {
    fun performReset() {
        Log.d(TAG, "performReset: performing reset")
        repository.updateProfile(
            mapOf(
                LAST_RESET to Calendar.getInstance().timeInMillis,
                MONTHLY_EXPENSE to 0f,
                MONTHLY_INCOME to 0f
            )
        )
    }

    private val repository: Repository = Repository.getInstance()
    private var _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    val userData: LiveData<User> = repository.userData

    init {
        viewModelScope.launch { repository.getTransactions(3).collect { _transactions.value = it } }
    }
}
