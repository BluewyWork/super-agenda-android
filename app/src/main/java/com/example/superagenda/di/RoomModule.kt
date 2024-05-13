package com.example.superagenda.di

import android.content.Context
import androidx.room.Room
import com.example.superagenda.data.database.SuperAgendaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val SUPER_AGENDA_DATABASE_NAME = "SuperAgenda"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, SuperAgendaDatabase::class.java, SUPER_AGENDA_DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideTokenDao(db: SuperAgendaDatabase) = db.getTokenDao()
}