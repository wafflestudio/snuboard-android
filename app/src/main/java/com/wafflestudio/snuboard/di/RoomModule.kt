package com.wafflestudio.snuboard.di

import android.content.Context
import com.wafflestudio.snuboard.data.room.NoticeNotiDao
import com.wafflestudio.snuboard.data.room.NoticeNotiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideNoticeNotiDao(
            @ApplicationContext appContext: Context
    ): NoticeNotiDao =
            NoticeNotiDatabase.getInstance(appContext).noticeNotiDao()
}
