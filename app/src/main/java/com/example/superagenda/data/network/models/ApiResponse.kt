package com.example.superagenda.data.network.models

data class ApiResponse<T>(val result: T, val ok: Boolean)
