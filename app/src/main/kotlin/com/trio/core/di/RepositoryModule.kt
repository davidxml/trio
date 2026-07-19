package com.trio.core.di

import com.trio.data.repository.ModeRepositoryImpl
import com.trio.domain.repository.ModeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindModeRepository(impl: ModeRepositoryImpl): ModeRepository
}
