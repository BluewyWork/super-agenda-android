package com.example.superagenda.data.network.models

import com.example.superagenda.util.AppError

data class ApiResponse<T>(val success: T, val ok: Boolean)
