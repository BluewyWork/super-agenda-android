package com.example.superagenda.util

sealed interface AppError : Error {
   enum class NetworkError : AppError {
      NO_INTERNET,
      SERVER_ERROR,
      UNKNOWN;
   }

   enum class DatabaseError : AppError {
      UNKNOWN
   }
}