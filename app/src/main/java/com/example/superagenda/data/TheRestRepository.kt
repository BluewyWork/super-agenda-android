package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.TheRestDao
import com.example.superagenda.data.database.entities.TheRestEntity
import com.example.superagenda.data.models.bsonDateTimeToLocalDateTime
import com.example.superagenda.data.models.localDateTimeToBsonDateTime
import com.example.superagenda.data.models.localDateTimeToString
import com.example.superagenda.data.models.stringToLocalDateTime
import com.example.superagenda.data.network.TheRestApi
import com.example.superagenda.data.network.models.LastModifiedInResponse
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import com.example.superagenda.util.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class TheRestRepository @Inject constructor(
   private val theRestDao: TheRestDao,
   private val theRestApi: TheRestApi,
) {
   suspend fun getLastModifiedAtDatabase(): AppResult<LocalDateTime> {
      return withContext(Dispatchers.IO) {
         try {
            val value = theRestDao.get().lastModified
            Log.d("LOOK AT ME", "get: $value")

            val converted = stringToLocalDateTime(value)
               ?: return@withContext Result.Error(AppError.MainError.CONVERSION_FAILED)
            Log.d("LOOK AT ME", "get2: $converted")

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
            Log.d("LOOK AT ME", "raw: ${lastModified.toString()}")
            val converted = localDateTimeToString(lastModified)
               ?: return@withContext Result.Error(AppError.MainError.CONVERSION_FAILED)
            Log.d("LOOK AT ME", "conv: $converted")

            theRestDao.upsert(TheRestEntity(lastModified = converted))
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
            apiCall = { theRestApi.getLastModified(token) }
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
            apiCall = { theRestApi.updateLastModified(token, LastModifiedInResponse(converted)) }
         ) {}
      }
   }
}