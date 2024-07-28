package com.example.superagenda.data.models

import com.example.superagenda.domain.models.UserForLogin
import com.google.gson.annotations.SerializedName

data class UserForLoginModel(
   @SerializedName("username") val username: String,
   @SerializedName("password") val password: String,
)

fun UserForLogin.toData() = UserForLoginModel(
   username = username,
   password = password
)