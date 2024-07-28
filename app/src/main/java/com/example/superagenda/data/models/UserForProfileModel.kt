package com.example.superagenda.data.models

import com.example.superagenda.domain.models.UserForProfile
import com.google.gson.annotations.SerializedName

data class UserForProfileModel(
   @SerializedName("username") val username: String,
)

fun UserForProfile.toData() = UserForProfileModel(
   username = username
)

fun UserForProfileModel.toDomain() = UserForProfile(
   username = username
)