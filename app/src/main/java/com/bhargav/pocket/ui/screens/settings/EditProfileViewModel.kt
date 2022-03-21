package com.bhargav.pocket.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.bhargav.pocket.repository.NAME
import com.bhargav.pocket.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class EditProfileViewModel : ViewModel() {
    val repository = Repository.getInstance()
    val userData = repository.userData

    fun editName(name: String) = repository.updateProfile(mapOf(NAME to name))
}
