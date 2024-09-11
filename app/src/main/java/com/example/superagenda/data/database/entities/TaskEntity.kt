package com.example.superagenda.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.superagenda.data.models.TaskModel
import com.example.superagenda.data.models.TaskStatusModel
import com.example.superagenda.data.models.localDateTimeToBsonDateTime
import com.example.superagenda.data.models.stringToLocalDateTime
import org.bson.types.ObjectId

@Entity(tableName = "task_table")
data class TaskEntity(
   @PrimaryKey(autoGenerate = false)
   @ColumnInfo(name = "_id") val _id: String,
   @ColumnInfo(name = "title") val title: String,
   @ColumnInfo("description") val description: String,
   @ColumnInfo("status") val status: TaskStatusModel,
   @ColumnInfo("startDateTime") val startDateTime: String,
   @ColumnInfo(name = "endDateTime") val endDateTime: String,
   @ColumnInfo(name = "lastModified") val lastModified: String,
)

fun TaskEntity.toData() = TaskModel(
   _id = ObjectId(_id),
   title = title,
   description = description,
   status = status,
   startDateTime = localDateTimeToBsonDateTime(stringToLocalDateTime(startDateTime)!!),
   endDateTime = localDateTimeToBsonDateTime(stringToLocalDateTime(endDateTime)!!),
   lastModified = localDateTimeToBsonDateTime(stringToLocalDateTime(lastModified)!!)
)