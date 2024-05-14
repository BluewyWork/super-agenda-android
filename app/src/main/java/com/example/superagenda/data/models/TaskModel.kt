package com.example.superagenda.data.models

import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.google.gson.annotations.SerializedName
import org.bson.types.ObjectId

data class TaskModel(
    @SerializedName("_id") val _id: ObjectId,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: TaskStatusModel
)

enum class TaskStatusModel {
    NotStarted,
    Ongoing,
    Completed
}

fun TaskModel.toDomain() = Task(
    _id = _id,
    title = title,
    description = description,
    status = when (status) {
        TaskStatusModel.NotStarted -> TaskStatus.NotStarted
        TaskStatusModel.Ongoing -> TaskStatus.Ongoing
        TaskStatusModel.Completed -> TaskStatus.Completed
    }
)