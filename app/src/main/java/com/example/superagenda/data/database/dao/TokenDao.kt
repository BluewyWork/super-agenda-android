package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.superagenda.data.database.entities.TokenEntity

@Dao
interface TokenDao {
    @Query("SELECT * FROM token_table")
    fun get(): TokenEntity

    @Query("DELETE FROM token_table")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tokenEntity: TokenEntity)
}
