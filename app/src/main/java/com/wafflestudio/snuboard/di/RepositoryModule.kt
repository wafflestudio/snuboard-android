package com.wafflestudio.snuboard.di

import com.wafflestudio.snuboard.data.repository.UserRepository
import com.wafflestudio.snuboard.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideUserRepositoryImpl(repository: UserRepositoryImpl): UserRepository
}