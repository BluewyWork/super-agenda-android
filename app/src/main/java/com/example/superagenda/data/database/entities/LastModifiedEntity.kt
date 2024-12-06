package com.example.superagenda.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "last_modified")
data class LastModifiedEntity(
   @PrimaryKey(autoGenerate = false)
   @ColumnInfo("lastModified") val lastModified: String
)