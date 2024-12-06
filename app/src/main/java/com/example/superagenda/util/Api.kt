package com.example.superagenda.util

import android.util.Log
import com.example.superagenda.data.network.models.ApiResponse
import retrofit2.HttpException

inline fun <V, T> safeApiCall(
   apiCall: () -> ApiResponse<V>,
   successHandler: (ApiResponse<V>) -> T,
): AppResult<T> {
   return try {
      val result = successHandler(apiCall())
      return Result.Success(result)
   } catch (e: HttpException) {
      Log.e("LOOK AT ME", "$e")

      when (e.code()) {
         401 -> Result.Error(AppError.NetworkError.UNAUTHORIZED)
         402 -> Result.Error(AppError.NetworkError.CONFLICT)
         408 -> Result.Error(AppError.NetworkError.REQUEST_TIMEOUT)
         413 -> Result.Error(AppError.NetworkError.PAYLOAD_TOO_LARGE)
         in 500..599 -> Result.Error(AppError.NetworkError.SERVER_ERROR)
         else -> Result.Error(AppError.NetworkError.UNKNOWN)
      }
   } catch (e: Exception) {
      Log.e("LOOK AT ME", "$e")
      Result.Error(AppError.NetworkError.UNKNOWN)
   }
}