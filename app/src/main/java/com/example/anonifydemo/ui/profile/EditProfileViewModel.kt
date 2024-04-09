package com.example.anonifydemo.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.repository.AppRepository
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel() {
    fun updateBio(userId : String, bio: String, result : (String) -> Unit) {

        viewModelScope.launch {
            val res = AppRepository.updateBio(userId,bio)
            result(res)
        }

    }
    // TODO: Implement the ViewModel
}