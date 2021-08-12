package com.wafflestudio.snuboard.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoticeNoti::class], version = 1, exportSchema = false)
abstract class NoticeNotiDatabase : RoomDatabase() {

    abstract fun noticeNotiDao(): NoticeNotiDao

    companion object {
        @Volatile
        private var INSTANCE: NoticeNotiDatabase? = null
       
        @JvmStatic
        fun getInstance(context: Context): NoticeNotiDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                NoticeNotiDatabase::class.java,
                "notice_noti_db"
            ).build()
        }
    }
}
