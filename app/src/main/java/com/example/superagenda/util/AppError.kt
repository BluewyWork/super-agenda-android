package com.example.superagenda.util

sealed interface AppError : Error {
   enum class NetworkError : AppError {
      NO_INTERNET,
      SERVER_ERROR,
      UNKNOWN {
         override fun toString(): String {
            return "NetworkError::UNKNOWN"
         }
      }
   }

   enum class DatabaseError : AppError {
      UNKNOWN {
         override fun toString(): String {
            return "DatabaseError::UNKNOWN"
         }
      }
   }
}