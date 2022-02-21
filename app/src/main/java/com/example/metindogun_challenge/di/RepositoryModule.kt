package com.example.metindogun_challenge.di

import com.example.metindogun_challenge.data.repository.LocationRepository
import com.example.metindogun_challenge.data.repository.LocationRepositoryImpl
import com.example.metindogun_challenge.data.repository.PhotoRepository
import com.example.metindogun_challenge.data.repository.PhotoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providePhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    abstract fun provideLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository

}