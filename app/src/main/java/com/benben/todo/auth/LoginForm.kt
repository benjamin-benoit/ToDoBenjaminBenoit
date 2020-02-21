package com.benben.todo.auth

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class LoginForm(
    @field:Json(name = "firstname")
    val firstName: String? = "",
    @field:Json(name = "lastname")
    val lastname: String? = "",
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "password")
    val password: String,
    @field:Json(name = "password_confirmation")
    val passwordConfirmation: String? = ""
) : Serializable, Parcelable