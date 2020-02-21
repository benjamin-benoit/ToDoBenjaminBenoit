package com.benben.todo.auth

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class LoginForm(
    @field:Json(name = "email")
    val id: String,
    @field:Json(name = "password")
    val title: String
): Serializable, Parcelable