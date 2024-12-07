package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.superagenda.data.database.entities.MiscEntity

@Dao
interface MiscDao {
   @Upsert
   fun upsert(miscEntity: MiscEntity)

   @Query("SELECT * FROM misc_table LIMIT 1")
   fun get(): MiscEntity

   @Query("DELETE FROM misc_table")
   fun delete()
}
