package com.benben.todo.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserInfoViewModel : ViewModel() {
    private val userInfoRepository: UserInfoRepository = UserInfoRepository()
    private val _userInfo = MutableLiveData<UserInfo>()
    public val userInfo: LiveData<UserInfo> = _userInfo

    private fun getMutableList() = _userInfo.value

    suspend fun getUser() {
        viewModelScope.launch {
            val editedUser = userInfoRepository.getUserInfo()
            _userInfo.value = getMutableList().apply {
                onCleared()
                editedUser?.let { it }
            }
        }
    }

    suspend fun editUser(userInfo: UserInfo) {
        viewModelScope.launch {
            val editedUser = userInfoRepository.updateTask(userInfo) ?: return@launch
            _userInfo.value = getMutableList().apply {
            }
        }
    }
}