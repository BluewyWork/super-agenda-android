package com.example.superagenda.data.network.models

data class ApiResponse<T>(val success: T, val ok: Boolean)
