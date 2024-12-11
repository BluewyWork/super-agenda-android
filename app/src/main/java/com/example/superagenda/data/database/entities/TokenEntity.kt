package com.example.superagenda.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token_table")
data class TokenEntity(
   @PrimaryKey val id: Int = 0,
   @ColumnInfo(name = "token") val token: String,
)
