package com.example.superagenda.data.models

import com.example.superagenda.domain.models.UserForRegister
import com.google.gson.annotations.SerializedName

data class UserForRegisterModel(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

fun UserForRegister.toData() = UserForRegisterModel(
    username = username,
    password = password
)