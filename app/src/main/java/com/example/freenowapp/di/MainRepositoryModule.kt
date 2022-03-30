package com.example.freenowapp.di

import com.example.freenowapp.repository.MainRepository
import com.example.freenowapp.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainRepositoryModule {

    @Binds
    abstract fun bindMainRepository(mainRepository: MainRepositoryImpl ): MainRepository

}