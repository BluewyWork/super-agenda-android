package com.example.superagenda.domain.models

import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Task(
   val id: ObjectId,
   var title: String,
   val description: String,
   val status: TaskStatus,
   val startDateTime: LocalDateTime,
   val endDateTime: LocalDateTime?,
   val endEstimatedDateTime: LocalDateTime,
   val images: List<String>,
)

enum class TaskStatus {
   NotStarted,
   Ongoing,
   Completed
}