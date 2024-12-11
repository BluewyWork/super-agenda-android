package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.MiscDao
import com.example.superagenda.data.database.entities.MiscEntity
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MiscRepository @Inject constructor(
   private val miscDao: MiscDao,
) {
   suspend fun getScreenShownAtDatabase(): AppResult<Boolean> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(miscDao.get().sliderShown)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun updateScreenShownAtDatabase(shown: Boolean): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(miscDao.upsert(MiscEntity(sliderShown = shown)))
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }
}