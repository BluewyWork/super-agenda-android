package com.example.superagenda.data.network.response

data class ApiResponse<T>(val data: T, val ok: Boolean)