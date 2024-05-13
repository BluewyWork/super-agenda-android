package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.entities.TokenEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val tokenDao: TokenDao
) {
    suspend fun retrieveTokenFromApi() {

    }

    suspend fun insertTokenToDatabase(token: String) {
        return withContext(Dispatchers.IO) {
            try {
                val tokenEntity = TokenEntity(token)
                tokenDao.insert(tokenEntity)
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")
            }
        }
    }

    suspend fun getGuestTokenFromDatabase() {
        return withContext(Dispatchers.IO) {
            try {
                tokenDao.get()
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")
            }
        }
    }

    suspend fun deleteTokenTableContents() {
        return withContext(Dispatchers.IO) {
            try {
                tokenDao.deleteAll()
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")
            }
        }
    }
}
