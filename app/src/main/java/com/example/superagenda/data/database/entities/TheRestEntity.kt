package com.example.superagenda.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "the_rest_table")
data class TheRestEntity(
   @PrimaryKey val id: Int = 0,
   @ColumnInfo("lastModified") val lastModified: String
)