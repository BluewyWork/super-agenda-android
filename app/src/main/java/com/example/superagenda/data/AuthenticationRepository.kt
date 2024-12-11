package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.entities.TokenEntity
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.network.AuthenticationApi
import com.example.superagenda.domain.models.UserForLogin
import com.example.superagenda.domain.models.UserForRegister
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import com.example.superagenda.util.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
   private val authenticationApi: AuthenticationApi,
   private val tokenDao: TokenDao,
) {
   suspend fun getTokenAtApi(userForLogin: UserForLogin): AppResult<String> {
      return withContext(Dispatchers.IO) {
         safeApiCall(
            apiCall = {
               authenticationApi.login(userForLogin.toData())
            }
         ) {
            it.result.token
         }
      }
   }

   suspend fun upsertTokenAtDatabase(token: String): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            tokenDao.upsert(TokenEntity(token =token))
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
         val userForRegisterModel = userForRegister.toData()

         safeApiCall(
            apiCall = {
               authenticationApi.register(userForRegisterModel)
            }
         ) {}
      }
   }
}


