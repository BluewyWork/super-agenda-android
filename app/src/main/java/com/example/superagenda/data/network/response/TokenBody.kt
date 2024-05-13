package com.example.superagenda.data.network.response

import com.google.gson.annotations.SerializedName

data class TokenBody(@SerializedName("token") val token: String)