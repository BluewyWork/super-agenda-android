package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.LastModifiedDao
import com.example.superagenda.data.database.entities.LastModifiedEntity
import com.example.superagenda.data.models.bsonDateTimeToLocalDateTime
import com.example.superagenda.data.models.localDateTimeToBsonDateTime
import com.example.superagenda.data.models.localDateTimeToString
import com.example.superagenda.data.models.stringToLocalDateTime
import com.example.superagenda.data.network.MiscApi
import com.example.superagenda.data.network.models.LastModifiedInResponse
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import com.example.superagenda.util.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class LastModifiedRepository @Inject constructor(
   private val lastModifiedDao: LastModifiedDao,
   private val miscApi: MiscApi,
) {
   suspend fun getLastModifiedAtDatabase(): AppResult<LocalDateTime> {
      return withContext(Dispatchers.IO) {
         try {
            val value = lastModifiedDao.get().lastModified

            val converted = stringToLocalDateTime(value)
               ?: return@withContext Result.Error(AppError.MainError.CONVERSION_FAILED)

            Result.Success(converted)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun upsertLastModifiedAtDatabase(lastModified: LocalDateTime): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            val converted = localDateTimeToString(lastModified)
               ?: return@withContext Result.Error(AppError.MainError.CONVERSION_FAILED)

            lastModifiedDao.upsert(LastModifiedEntity(converted))
            Result.Success(Unit)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun getLastModifiedAtApi(token: String): AppResult<LocalDateTime?> {
      return withContext(Dispatchers.IO) {
         safeApiCall(
            apiCall = { miscApi.getLastModified(token) }
         ) {
            it.result.lastModified?.let { it1 -> bsonDateTimeToLocalDateTime(it1) }
         }
      }
   }

   suspend fun updateLastModifiedAtApi(
      token: String,
      lastModified: LocalDateTime,
   ): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         val converted = localDateTimeToBsonDateTime(lastModified)

         safeApiCall(
            apiCall = { miscApi.updateLastModified(token, LastModifiedInResponse(converted)) }
         ) {}
      }
   }
}