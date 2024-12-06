package com.example.superagenda.di

import android.content.Context
import androidx.room.Room
import com.example.superagenda.data.database.AppDatabase
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
      Room.databaseBuilder(context, AppDatabase::class.java, SUPER_AGENDA_DATABASE_NAME)
         .build()

   @Singleton
   @Provides
   fun provideTokenDao(db: AppDatabase) = db.tokenDao()

   @Singleton
   @Provides
   fun provideTaskDao(db: AppDatabase) = db.taskDao()

   @Singleton
   @Provides
   fun provideProfile(db: AppDatabase) = db.userForProfileDao()

   @Singleton
   @Provides
   fun provideLastModifiedDao(db: AppDatabase) = db.lastModifiedDao()
}