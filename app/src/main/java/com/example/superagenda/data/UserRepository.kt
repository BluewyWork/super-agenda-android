package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.UserForProfileDao
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.models.toDatabase
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.SelfApi
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.superagenda.util.Result

class SelfRepository @Inject constructor(
   private val selfApi: SelfApi,
   private val selfDao: UserForProfileDao
) {
   suspend fun retrieveProfileFromAPI(token: String): UserForProfile? {
      return withContext(Dispatchers.IO) {
         try {
            selfApi.retrieveUserProfile(token).data.toDomain()
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            null
         }
      }
   }

   suspend fun deleteProfileFromApi(token: String): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            val response = selfApi.deleteProfile(token)

            return@withContext response.ok
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            false
         }
      }
   }

   suspend fun upsertProfileAtDatabase(userForProfile: UserForProfile): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            selfDao.upsert(userForProfile.toData().toDatabase())

            Result.Success(Unit)
         } catch (e: Exception) {
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }
}