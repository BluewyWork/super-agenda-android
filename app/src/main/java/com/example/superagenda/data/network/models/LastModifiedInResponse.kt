package com.example.superagenda.data.network.models

import com.example.superagenda.data.models.BsonDateTimeConverter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.bson.BsonDateTime

data class LastModifiedInResponse(
   @JsonAdapter(BsonDateTimeConverter::class)
   @SerializedName("last_modified") val lastModified: BsonDateTime?,
)