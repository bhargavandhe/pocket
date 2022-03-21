package com.bhargav.pocket.ui.screens.register

import androidx.lifecycle.ViewModel
import com.bhargav.pocket.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RegisterScreenViewModel : ViewModel() {
    private val repository: Repository = Repository.getInstance()

    fun createNewUser(
        email: String,
        password: String,
        name: String,
        balance: Float
    ) = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener {
            Repository.getInstance().init()
            repository.setNewUser(email = email, name = name, balance = balance)
        }
}
