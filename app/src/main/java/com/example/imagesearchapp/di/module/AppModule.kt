package com.example.imagesearchapp.di.module

import com.example.imagesearchapp.api.UnSplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
         Retrofit.Builder().baseUrl(UnSplashApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()


    @Provides
    fun provideUnSplashApi(retrofit:Retrofit):UnSplashApi =
         retrofit.create(UnSplashApi::class.java)

}