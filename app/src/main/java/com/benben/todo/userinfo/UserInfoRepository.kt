package com.benben.todo.userinfo


import Task
import com.benben.todo.auth.LoginForm
import com.benben.todo.auth.LoginResponse
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

    suspend fun updateUser(user: UserInfo): UserInfo? {
        val userResponse = userService.update(user)
        return if (userResponse.isSuccessful) {
            userResponse.body()
        } else null
    }
    suspend fun login(user: LoginForm): LoginResponse? {
        val userResponse = userService.login(user)
        return if (userResponse.isSuccessful) {
            userResponse.body()
        } else null
    }
    suspend fun signUp(user: LoginForm): LoginResponse? {
        val userResponse = userService.login(user)
        return if (userResponse.isSuccessful) {
            userResponse.body()
        } else null
    }
}