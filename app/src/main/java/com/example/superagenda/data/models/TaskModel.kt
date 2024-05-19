package com.example.superagenda.data.models

import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.bson.types.ObjectId
import java.lang.reflect.Type

data class TaskModel(
    @JsonAdapter(ObjectIdSerializer::class)
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

fun Task.toData() = TaskModel(
    _id = _id,
    title = title,
    description = description,
    status = when (status) {
        TaskStatus.NotStarted -> TaskStatusModel.NotStarted
        TaskStatus.Ongoing -> TaskStatusModel.Ongoing
        TaskStatus.Completed -> TaskStatusModel.Completed
    }
)

// need this because mongodb kotlin driver is wonky

//class ObjectIdSerializer : JsonSerializer<ObjectId> {
//    override fun serialize(
//        src: ObjectId?,
//        typeOfSrc: Type?,
//        context: JsonSerializationContext?
//    ): JsonElement {
//        return if (src == null) {
//            JsonNull.INSTANCE
//        } else {
//            JsonObject().apply {
//                addProperty("\$oid", src.toHexString())
//            }
//        }
//    }
//}

class ObjectIdSerializer : JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {
    override fun serialize(
        src: ObjectId?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return if (src == null) {
            JsonNull.INSTANCE
        } else {
            JsonObject().apply {
                addProperty("\$oid", src.toHexString())
            }
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ObjectId? {
        if (json == null || json.isJsonNull) {
            return null
        }
        return if (json.isJsonObject && json.asJsonObject.has("\$oid")) {
            ObjectId(json.asJsonObject.get("\$oid").asString)
        } else {
            ObjectId(json.asString)
        }
    }
}