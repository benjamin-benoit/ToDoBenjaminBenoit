package com.benben.todo.userinfo

import com.squareup.moshi.Json

data class UserInfo(
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "firstname")
    val firstName: String,
    @field:Json(name = "lastname")
    val lastName: String,
    @field:Json(name = "avatar")
    val avatar: String
)