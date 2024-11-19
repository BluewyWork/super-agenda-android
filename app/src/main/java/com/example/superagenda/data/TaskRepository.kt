package com.example.superagenda.data

import android.os.Environment
import android.util.Log
import com.example.superagenda.data.database.dao.TaskDao
import com.example.superagenda.data.database.entities.toData
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.models.toDatabase
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.TaskApi
import com.example.superagenda.domain.models.Task
import com.example.superagenda.util.AppError
import com.example.superagenda.util.AppResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import com.example.superagenda.util.Result

class TaskRepository @Inject constructor(
   private val taskApi: TaskApi,
   private val taskDao: TaskDao,
) {
   suspend fun getTasksAtDatabase(): AppResult<List<Task>> {
      return withContext(Dispatchers.IO) {
         try {
            val taskList = taskDao.retrieveTasks()

            Result.Success(taskList.map { it.toData().toDomain() })
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }

   suspend fun upsertTaskAtDatabase(task: Task): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            taskDao.upsert(task.toData().toDatabase())

            Result.Success(Unit)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun deleteTaskAtDatabase(taskID: ObjectId): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            taskDao.deleteTask(taskID.toHexString())

            Result.Success(Unit)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun deleteTasksAtDatabase(): AppResult<Unit> {
      return withContext(Dispatchers.IO) {
         try {
            taskDao.nukeTasks()

            Result.Success(Unit)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.DatabaseError.UNKNOWN)
         }
      }
   }

   suspend fun getTasksAtAPI(token: String): AppResult<List<Task>> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(taskApi.retrieveTaskList(token).data.map { it.toDomain() })
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }

   suspend fun createTaskAtAPI(task: Task, token: String): AppResult<Boolean> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(taskApi.createTask(token, task.toData()).ok)

         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }

   suspend fun updateTaskAtAPI(task: Task, token: String): AppResult<Boolean> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(
               taskApi.updateTask(token, task.toData()).ok
            )
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }

   suspend fun deleteTaskAtApi(taskID: ObjectId, token: String): AppResult<Boolean> {
      return withContext(Dispatchers.IO) {
         try {
            Result.Success(taskApi.deleteTask(token, taskID.toHexString()).ok)
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            Result.Error(AppError.NetworkError.UNKNOWN)
         }
      }
   }

   suspend fun backupTasksAtLocalStorage(tasks: List<Task>): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            val gson = Gson()
            val taskListJson = gson.toJson(tasks.map { it.toData() })

            val directory =
               Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            if (!directory.exists()) {
               directory.mkdirs()
            }

            val fileName =
               SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date()) + ".json"
            val file = File(directory, fileName)


            val outputStream = FileOutputStream(file)
            outputStream.write(taskListJson.toByteArray())
            outputStream.close()

            true
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")
            false
         }
      }
   }
}
