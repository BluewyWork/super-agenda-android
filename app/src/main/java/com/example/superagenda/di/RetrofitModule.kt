package com.example.superagenda.di

import com.example.superagenda.data.network.AuthenticationApi
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
        return Retrofit.Builder().baseUrl("http://10.0.2.2:8001")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideAuthenticationApiClient(retrofit: Retrofit): AuthenticationApi {
        return retrofit.create(AuthenticationApi::class.java)
    }
}
