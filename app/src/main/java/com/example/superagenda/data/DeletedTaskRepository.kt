package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.network.DeletedTaskApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import javax.inject.Inject

class DeletedTaskRepository @Inject constructor(
   private val deletedTaskApi: DeletedTaskApi
) {
   suspend fun retrieveDeletedTasksAtApi(token: String):List<ObjectId>? {
      return withContext(Dispatchers.IO) {
         try {
            deletedTaskApi.getDeletedTasks(token).data
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            null
         }
      }
   }

   suspend fun addDeletedTaskAtApi(token: String, objectId: ObjectId): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            deletedTaskApi.postDeletedTask(token, objectId)
            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            false
         }
      }
   }
}