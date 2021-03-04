package com.wafflestudio.snuboard.di

import com.wafflestudio.snuboard.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideUserRepositoryImpl(repository: UserRepositoryImpl): UserRepository

    @Binds
    fun provideDepartmentRepositoryImpl(repository: DepartmentRepositoryImpl): DepartmentRepository

    @Binds
    fun provideNoticeRepositoryImpl(repository: NoticeRepositoryImpl): NoticeRepository
}