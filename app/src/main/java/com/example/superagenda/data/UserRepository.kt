package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.UserForProfileDao
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.models.toDatabase
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.UserApi
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
   private val userApi: UserApi,
   private val userForProfileDao: UserForProfileDao,
) {
   suspend fun getUserForProfileAtApi(token: String): AppResult<UserForProfile> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(userApi.getUserForProfile(token).data.toDomain())
         } catch (e: Exception) {
            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }

   suspend fun deleteUserAtApi(token: String): AppResult<Boolean> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(userApi.delete(token).ok)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }

   suspend fun upsertUserForProfileAtDatabase(userForProfile: UserForProfile): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            userForProfileDao.upsert(userForProfile.toData().toDatabase())

            Result.Success(Unit)
         } catch (e: Exception) {
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun getUserForProfileAtDatabase(): AppResult<UserForProfile> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(userForProfileDao.get().toData().toDomain())
         } catch (e: Exception) {
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }
}