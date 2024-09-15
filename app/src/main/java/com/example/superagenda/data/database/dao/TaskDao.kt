package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.superagenda.data.database.entities.TaskEntity

@Dao
interface TaskDao {
   @Query("SELECT * FROM task_table")
   fun retrieveTasks(): List<TaskEntity>

   @Query("DELETE FROM task_table")
   fun nukeTasks()

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertOrUpdateTasks(taskEntityList: List<TaskEntity>)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertOrUpdateTask(taskEntity: TaskEntity)

   @Query("DELETE FROM task_table WHERE id = :taskId")
   fun deleteTask(taskId: String)
}