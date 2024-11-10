package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.entities.TokenEntity
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.network.AuthenticationApi
import com.example.superagenda.domain.models.UserForLogin
import com.example.superagenda.domain.models.UserForRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
   private val authenticationApi: AuthenticationApi,
   private val tokenDao: TokenDao,
) {
   suspend fun retrieveTokenFromAPI(userForLogin: UserForLogin): String? {
      return withContext(Dispatchers.IO) {
         try {
            val userForLoginModel = userForLogin.toData()
            val apiResponse = authenticationApi.login(userForLoginModel)

            apiResponse.data.token
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            null
         }
      }
   }

   suspend fun insertTokenToLocalDatabase(token: String): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            val tokenEntity = TokenEntity(token)
            tokenDao.upsert(tokenEntity)

            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            false
         }
      }
   }

   suspend fun retrieveTokenFromLocalStorage(): String? {
      return withContext(Dispatchers.IO) {
         try {
            tokenDao.get().token
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            null
         }
      }
   }

   suspend fun clearTokensFromLocalStorage(): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            tokenDao.delete()

            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            false
         }
      }
   }

   suspend fun registerAtAPI(userForRegister: UserForRegister): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            val userForRegisterModel = userForRegister.toData()

            val response = authenticationApi.register(userForRegisterModel)

            response.ok
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            false
         }
      }
   }
}