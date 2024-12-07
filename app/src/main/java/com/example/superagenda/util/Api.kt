package com.example.superagenda.util

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response

inline fun <V, T> safeApiCall(
   apiCall: () -> Response<T>,
   successHandler: (T) -> V,
): AppResult<V> {
   return try {
      val response = apiCall()

      when (val code = response.code()) {
         in 200..299 -> {
            val body = response.body() ?: return Result.Error(AppError.NetworkError.SERIALIZATION)
            val result = successHandler(body)
            Result.Success(result)
         }

         401 -> Result.Error(AppError.NetworkError.UNAUTHORIZED)
         408 -> Result.Error(AppError.NetworkError.REQUEST_TIMEOUT)
         409 -> Result.Error(AppError.NetworkError.CONFLICT)
         413 -> Result.Error(AppError.NetworkError.PAYLOAD_TOO_LARGE)
         in 500..599 -> Result.Error(AppError.NetworkError.SERVER_ERROR)
         else -> Result.Error(AppError.NetworkError.UNKNOWN)
      }
   } catch (e: HttpException) {
      Log.e("LOOK AT ME", "${e.message}")
      Result.Error(AppError.NetworkError.UNKNOWN)
   } catch (e: Exception) {
      Log.e("LOOK AT ME", "${e.message}")
      Result.Error(AppError.NetworkError.UNKNOWN)
   }
}