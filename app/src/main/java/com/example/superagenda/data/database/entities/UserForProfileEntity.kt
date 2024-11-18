package com.example.superagenda.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.superagenda.data.models.MembershipModel

@Entity(tableName = "profile_table")
data class UserForProfileEntity(
   @PrimaryKey(autoGenerate = false)
   @ColumnInfo(name = "username") val username: String,
   @ColumnInfo(name = "membership") val membership: MembershipModel
)