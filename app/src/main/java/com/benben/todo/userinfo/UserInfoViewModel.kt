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

    fun editUser(userInfo: UserInfo) {
        viewModelScope.launch {
            val editedUser = userInfoRepository.updateUser(userInfo) ?: return@launch
            _userInfo.value = editedUser
        }
    }
}