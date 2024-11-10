package com.example.superagenda.data.network.models

data class ApiResponse<T>(val data: T, val ok: Boolean)