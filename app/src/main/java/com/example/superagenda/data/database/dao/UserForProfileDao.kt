package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.superagenda.data.database.entities.UserForProfileEntity
import com.example.superagenda.data.database.entities.TokenEntity
import com.example.superagenda.domain.models.UserForProfile

@Dao
interface UserForProfileDao {
   @Query("SELECT * FROM profile_table LIMIT 1")
   fun get(): UserForProfileEntity

   @Upsert
   fun upsert(tokenEntity: UserForProfileEntity)

   @Query("DELETE FROM profile_table")
   fun delete()
}