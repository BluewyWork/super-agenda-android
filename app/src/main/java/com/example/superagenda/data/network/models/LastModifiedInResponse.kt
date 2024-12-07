package com.example.superagenda.data.network.models

import com.google.gson.annotations.SerializedName
import org.bson.BsonDateTime

data class LastModifiedInResponse(@SerializedName("last_modified") val lastModified: BsonDateTime?)