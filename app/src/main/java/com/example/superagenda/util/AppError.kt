package com.example.superagenda.util

sealed interface AppError : Error {
   enum class ClientError : AppError {
      USERNAME_TOO_SHORT,
      PASSWORD_TOO_SHORT,
      INVALID_CREDENTIALS,
      UNEXPECTED_BODY,
      SERVICE_ERROR,
      USERNAME_IS_TAKEN
   }

   enum class NetworkError : AppError {
      SERIALIZATION,
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