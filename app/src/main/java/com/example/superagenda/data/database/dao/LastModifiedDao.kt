package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.superagenda.data.database.entities.LastModifiedEntity
import com.example.superagenda.data.database.entities.TokenEntity

@Dao
interface LastModifiedDao {
   @Upsert
   fun upsert(lastModifiedEntity: LastModifiedEntity)

   @Query("SELECT * FROM last_modified LIMIT 1")
   fun get(): LastModifiedEntity

   @Query("DELETE FROM last_modified")
   fun delete()
}