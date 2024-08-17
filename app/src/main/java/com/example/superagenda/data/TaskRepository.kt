package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.database.dao.TaskDao
import com.example.superagenda.data.database.entities.toData
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.models.toDatabase
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.TaskApi
import com.example.superagenda.domain.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import javax.inject.Inject

class TaskRepository @Inject constructor(
   private val taskApi: TaskApi,
   private val taskDao: TaskDao,
) {
   suspend fun retrieveTasksFromLocalDatabase(): List<Task>? {
      return withContext(Dispatchers.IO) {
         try {
            val taskList = taskDao.selectAll()

            taskList.map { it.toData().toDomain() }
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            null
         }
      }
   }

   suspend fun insertOrUpdateTaskAtLocalDatabase(task: Task): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            taskDao.insertOrUpdate(task.toData().toDatabase())

            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            false
         }
      }
   }

   suspend fun deleteTaskAtLocalDatabase(taskID: ObjectId): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            taskDao.deleteById(taskID.toHexString())
            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            false
         }
      }
   }

   suspend fun deleteAllTasksAtLocalDatabase(): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            taskDao.deleteAll()
            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            false
         }
      }
   }

   suspend fun retrieveTasksAPI(token: String): List<Task>? {
      return withContext(Dispatchers.IO) {
         try {
            taskApi.retrieveTaskList(token).data.map { it.toDomain() }
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            null
         }
      }
   }

   suspend fun createTaskAtAPI(task: Task, token: String): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            taskApi.createTask(token, task.toData())

            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            false
         }
      }
   }

   suspend fun updateTaskAtAPI(task: Task, token: String): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            taskApi.updateTask(token, task.toData())
            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            false
         }
      }
   }

   suspend fun deleteTaskAtApi(taskID: ObjectId, token: String): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            taskApi.deleteTask(token, taskID.toHexString())
            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            false
         }
      }
   }
}
