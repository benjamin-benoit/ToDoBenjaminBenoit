package com.benben.todo.userinfo


import com.benben.todo.network.Api

class UserInfoRepository {
    private val userService = Api.userService

    suspend fun updateTask(user: UserInfo): UserInfo? {
        val userResponse = userService.update(user)
        return if (userResponse.isSuccessful) {
            userResponse.body()
        } else null
    }
}