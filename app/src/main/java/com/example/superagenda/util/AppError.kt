package com.example.superagenda.util

sealed interface AppError : Error {
   enum class MainError : AppError {
      CONVERSION_FAILED
   }

   enum class ClientError : AppError {
      USERNAME_TOO_SHORT,
      PASSWORD_TOO_SHORT,
      INVALID_CREDENTIALS,
      UNEXPECTED_BODY,
      SERVICE_ERROR,
      USERNAME_IS_TAKEN
   }

   enum class NetworkError : AppError {
      REQUEST_TIMEOUT,
      UNAUTHORIZED,
      CONFLICT,
      TOO_MANY_REQUESTS,
      NO_INTERNET,
      PAYLOAD_TOO_LARGE,
      SERVER_ERROR,
      SERIALIZATION,
      MISSING_PAYLOAD,

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