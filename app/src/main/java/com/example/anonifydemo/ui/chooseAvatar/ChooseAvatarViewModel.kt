package com.example.anonifydemo.ui.chooseAvatar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.dataClasses.Avatar
import com.example.anonifydemo.ui.repository.AppRepository
import kotlinx.coroutines.launch

class ChooseAvatarViewModel : ViewModel() {

    private var _avatarList = MutableLiveData<List<Avatar>>()

    val avatarList : LiveData<List<Avatar>> = _avatarList

    init {
        viewModelScope.launch {
            AppRepository.getAvatars()
        }
    }
//    suspend fun getAvatars(): LiveData<List<Avatar>> {
//        _avatarListavatarListAppRepository.getAvatars()
//    }
}