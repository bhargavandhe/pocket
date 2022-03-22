package com.bhargav.pocket.ui.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhargav.pocket.model.Categories
import com.bhargav.pocket.model.CategoryData
import com.bhargav.pocket.model.Transaction
import com.bhargav.pocket.model.User
import com.bhargav.pocket.repository.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "HomeScreenViewModel"

@ExperimentalCoroutinesApi
class HomeScreenViewModel : ViewModel() {
    fun performReset() {

        val spendings = mutableMapOf<String, CategoryData>()

        for (c in Categories.values()) spendings[c.name] = CategoryData(title = c.name)
        Log.d(com.bhargav.pocket.repository.TAG, "setNewUser: $spendings")

        Log.d(TAG, "performReset: performing reset")
        repository.updateProfile(
            mapOf(
                LAST_RESET to Calendar.getInstance().timeInMillis,
                MONTHLY_EXPENSE to 0f,
                MONTHLY_INCOME to 0f,
                SPENDINGS to spendings,
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
