package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.LastModifiedDao
import com.example.superagenda.data.database.entities.LastModifiedEntity
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class LastModifiedRepository @Inject constructor(
   private val lastModifiedDao: LastModifiedDao,
) {
   suspend fun getLastModifiedDao(): AppResult<LocalDateTime> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(lastModifiedDao.get().lastModified)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun upsertLastModifiedDao(lastModified: LocalDateTime): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            lastModifiedDao.upsert(LastModifiedEntity(lastModified))
            Result.Success(Unit)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }
}