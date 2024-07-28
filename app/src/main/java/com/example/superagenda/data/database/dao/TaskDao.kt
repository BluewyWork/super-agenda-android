package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.superagenda.data.database.entities.TaskEntity

@Dao
interface TaskDao
{
   @Query("SELECT * FROM task_table")
   fun selectAll(): List<TaskEntity>

   @Query("DELETE FROM task_table")
   fun deleteAll()

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertOrUpdateMany(taskEntityList: List<TaskEntity>)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertOrUpdate(taskEntity: TaskEntity)

   @Query("DELETE FROM task_table WHERE _id = :taskId")
   fun deleteById(taskId: String)
}