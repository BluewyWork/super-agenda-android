package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.entities.TokenEntity
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.network.TokenApi
import com.example.superagenda.domain.models.UserForLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val tokenApi: TokenApi,
    private val tokenDao: TokenDao
) {
    suspend fun retrieveTokenFromApi(userForLogin: UserForLogin): String? {
        return withContext(Dispatchers.IO) {
            try {
                val userForLoginModel = userForLogin.toData()
                val apiResponse = tokenApi.login(userForLoginModel)

                apiResponse.data.token
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                null
            }
        }
    }

    suspend fun saveTokenToLocalStorage(token: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val tokenEntity = TokenEntity(token)
                tokenDao.insert(tokenEntity)

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
                tokenDao.retrieve().token
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                null
            }
        }
    }

    suspend fun wipeAllTokensFromLocalStorage(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                tokenDao.deleteAll()

                true
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                false
            }
        }
    }
}