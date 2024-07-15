package com.example.superagenda.di

import com.example.superagenda.data.network.LoginApi
import com.example.superagenda.data.network.RegisterApi
import com.example.superagenda.data.network.SelfApi
import com.example.superagenda.data.network.TaskApi
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
    fun provideAuthenticationApiClient(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTaskApiClient(retrofit: Retrofit): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileApiClient(retrofit: Retrofit): SelfApi {
        return retrofit.create(SelfApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRegisterApiClient(retrofit: Retrofit): RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }
}
