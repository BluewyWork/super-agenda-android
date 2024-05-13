package com.example.superagenda.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

//    @Singleton
//    @Provides
//    fun provideGuestApiClient(retrofit: Retrofit): GuestApi {
//        return retrofit.create(GuestApi::class.java)
//    }
}
