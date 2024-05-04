package com.example.superagenda.domain.models

import org.bson.types.ObjectId

data class Task(
    val _id: ObjectId,
    val title: String,
    val description: String,
    val status: TaskStatus
)

enum class TaskStatus {
    NotStarted,
    Ongoing,
    Completed
}