package com.bhargav.pocket.ui.screens.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bhargav.pocket.model.User
import com.bhargav.pocket.repository.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

private const val TAG = "SettingsViewModel"

@ExperimentalCoroutinesApi
class SettingsViewModel : ViewModel() {
    private val repository: Repository = Repository.getInstance()
    val userData: LiveData<User> = repository.userData

    fun deleteUser(): Task<Void>? = repository.deleteCollection()?.addOnSuccessListener {
        FirebaseAuth.getInstance().currentUser?.delete()?.addOnCompleteListener {
            if (it.isSuccessful) Log.d(TAG, "deleteUser: Success") else Log.d(TAG, "deleteUser: ${it.exception}")
        }
    }
}
