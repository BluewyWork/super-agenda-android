package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.entities.TokenEntity
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.network.LoginApi
import com.example.superagenda.domain.models.UserForLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
   private val loginApi: LoginApi,
   private val tokenDao: TokenDao,
) {
   suspend fun retrieveTokenFromAPI(userForLogin: UserForLogin): String? {
      return withContext(Dispatchers.IO) {
         try {
            val userForLoginModel = userForLogin.toData()
            val apiResponse = loginApi.login(userForLoginModel)

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
            tokenDao.insertToken(tokenEntity)

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
            tokenDao.retrieveToken().token
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            null
         }
      }
   }

   suspend fun clearTokensFromLocalStorage(): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            tokenDao.nukeToken()

            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            false
         }
      }
   }
}