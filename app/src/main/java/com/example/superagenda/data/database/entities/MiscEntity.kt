package com.example.superagenda.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "misc_table")
data class MiscEntity(
   @PrimaryKey(autoGenerate = false)
   val sliderShown: Boolean,
)
