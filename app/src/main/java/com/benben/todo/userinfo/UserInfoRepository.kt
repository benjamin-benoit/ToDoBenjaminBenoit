package com.benben.todo.userinfo


import Task
import com.benben.todo.network.Api

class UserInfoRepository {
    private val userService = Api.userService

    suspend fun getUserInfo(): List<Task>? {
        val usersResponse = userService.getInfo()
        if (usersResponse.isSuccessful) {
            return usersResponse.body() as List<Task>
        }
        return null
    }

    suspend fun updateTask(user: UserInfo): UserInfo? {
        val userResponse = userService.update(user)
        return if (userResponse.isSuccessful) {
            userResponse.body()
        } else null
    }

}