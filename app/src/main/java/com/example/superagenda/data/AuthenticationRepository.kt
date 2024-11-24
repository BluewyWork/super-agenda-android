package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.entities.TokenEntity
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.network.AuthenticationApi
import com.example.superagenda.data.network.models.ApiResponse
import com.example.superagenda.domain.models.UserForLogin
import com.example.superagenda.domain.models.UserForRegister
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
   private val authenticationApi: AuthenticationApi,
   private val tokenDao: TokenDao,
) {
   suspend fun getTokenAtApi(userForLogin: UserForLogin): AppResult<String> {
      return withContext(Dispatchers.IO) {
         try {
//            Result.Success(authenticationApi.login(userForLogin.toData()).success.token)
            safeApiCall(
               apiCall = {
                  authenticationApi.login(userForLogin.toData())
               }
            ) {
               it.success.token
            }
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }

   suspend fun upsertTokenAtDatabase(token: String): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            tokenDao.upsert(TokenEntity(token))
            Result.Success(Unit)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun getTokenAtDatabase(): AppResult<String> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(tokenDao.get().token)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun deleteTokenAtDatabase(): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            tokenDao.delete()

            Result.Success(Unit)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun registerAtAPI(userForRegister: UserForRegister): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            val userForRegisterModel = userForRegister.toData()

            safeApiCall(
               apiCall = {
                  authenticationApi.register(userForRegisterModel)
               }
            ) {}
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }
}

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
