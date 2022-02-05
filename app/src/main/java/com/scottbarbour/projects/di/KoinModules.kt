package com.scottbarbour.projects.di

import com.scottbarbour.projects.networking.AlbumsService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val myKoinModules = module {

    single(named("retrofit")) {
        Retrofit.Builder()
            .baseUrl(AlbumsService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<AlbumsService> {
        get<Retrofit>(named("retrofit"))
            .create(AlbumsService::class.java)
    }
}