package com.example.superagenda.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "misc_table")
data class MiscEntity(
   @PrimaryKey val id: Int = 0,
   val sliderShown: Boolean,
)
