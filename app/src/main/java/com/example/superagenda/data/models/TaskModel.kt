package com.example.superagenda.data.models

import com.example.superagenda.data.database.entities.TaskEntity
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.bson.BsonDateTime
import org.bson.types.ObjectId
import java.lang.reflect.Type

data class TaskModel(
   @JsonAdapter(ObjectIdSerializer::class)
   @SerializedName("_id") val id: ObjectId,
   val title: String,
   val description: String,
   val status: TaskStatusModel,
   @JsonAdapter(BsonDateTimeConverter::class)
   @SerializedName("start_date_time") val startDateTime: BsonDateTime,
   @JsonAdapter(BsonDateTimeConverter::class)
   @SerializedName("end_date_time") val endDateTime: BsonDateTime,
   @SerializedName("end_date_time_estimated") val endEstimatedDateTime: BsonDateTime,
   val images: List<String>,
)

enum class TaskStatusModel {
   NotStarted,
   Ongoing,
   Completed
}

fun TaskModel.toDomain() = Task(
   id = id,
   title = title,
   description = description,
   status = when (status) {
      TaskStatusModel.NotStarted -> TaskStatus.NotStarted
      TaskStatusModel.Ongoing -> TaskStatus.Ongoing
      TaskStatusModel.Completed -> TaskStatus.Completed
   },
   startDateTime = bsonDateTimeToLocalDateTime(startDateTime),
   endDateTime = bsonDateTimeToLocalDateTime(endDateTime),
   images = images,
   endEstimatedDateTime = bsonDateTimeToLocalDateTime(endEstimatedDateTime)
)

fun Task.toData() = TaskModel(
   id = id,
   title = title,
   description = description,
   status = when (status) {
      TaskStatus.NotStarted -> TaskStatusModel.NotStarted
      TaskStatus.Ongoing -> TaskStatusModel.Ongoing
      TaskStatus.Completed -> TaskStatusModel.Completed
   },
   startDateTime = localDateTimeToBsonDateTime(startDateTime),
   endDateTime = localDateTimeToBsonDateTime(endDateTime),
   endEstimatedDateTime = localDateTimeToBsonDateTime(endEstimatedDateTime),
   images = images
)

fun TaskModel.toDatabase() = TaskEntity(
   id = id.toString(),
   title = title,
   description = description,
   status = status,
   startDateTime = localDateTimeToString(bsonDateTimeToLocalDateTime(startDateTime))!!,
   endDateTime = localDateTimeToString(bsonDateTimeToLocalDateTime(endDateTime))!!,
   images = images,
   endEstimatedDateTime = localDateTimeToString(bsonDateTimeToLocalDateTime(endEstimatedDateTime))!!,
)

fun TaskEntity.toData() = TaskModel(
   id = ObjectId(id),
   title = title,
   description = description,
   status = status,
   startDateTime = localDateTimeToBsonDateTime(stringToLocalDateTime(startDateTime)!!),
   endDateTime = localDateTimeToBsonDateTime(stringToLocalDateTime(endDateTime)!!),
   images = images,
   endEstimatedDateTime = localDateTimeToBsonDateTime(stringToLocalDateTime(endEstimatedDateTime)!!)
)

class ObjectIdSerializer : JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {
   override fun serialize(
      src: ObjectId?,
      typeOfSrc: Type?,
      context: JsonSerializationContext?,
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
      context: JsonDeserializationContext?,
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

class BsonDateTimeConverter : JsonSerializer<BsonDateTime>, JsonDeserializer<BsonDateTime> {
   override fun serialize(
      src: BsonDateTime?,
      typeOfSrc: Type?,
      context: JsonSerializationContext?,
   ): JsonElement {
      return if (src == null) {
         JsonNull.INSTANCE
      } else {
         JsonObject().apply {
            add("\$date", JsonObject().apply {
               add("\$numberLong", JsonPrimitive(src.value.toString()))
            })
         }
      }
   }

   override fun deserialize(
      json: JsonElement?,
      typeOfT: Type?,
      context: JsonDeserializationContext?,
   ): BsonDateTime {
      val longValue = json?.asJsonObject?.getAsJsonObject("\$date")?.get("\$numberLong")?.asLong
      return BsonDateTime(longValue ?: 0)
   }
}
