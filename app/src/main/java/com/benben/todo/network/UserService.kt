package com.benben.todo.network

import com.benben.todo.auth.LoginForm
import com.benben.todo.auth.LoginResponse
import com.benben.todo.userinfo.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>
}