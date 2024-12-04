package com.example.superagenda.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.superagenda.data.models.TaskStatusModel

@Entity(tableName = "task_table")
data class TaskEntity(
   @PrimaryKey(autoGenerate = false)
   @ColumnInfo(name = "id") val id: String,
   @ColumnInfo(name = "title") val title: String,
   @ColumnInfo("description") val description: String,
   @ColumnInfo("status") val status: TaskStatusModel,
   @ColumnInfo("startDateTime") val startDateTime: String,
   @ColumnInfo(name = "endDateTime") val endDateTime: String?,
   @ColumnInfo(name = "endEstimatedDateTime") val endEstimatedDateTime: String,
   @ColumnInfo(name = "images") val images: List<String>,
)
