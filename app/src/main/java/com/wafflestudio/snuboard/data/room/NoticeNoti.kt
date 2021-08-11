package com.wafflestudio.snuboard.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notice_noti_table")
data class NoticeNoti(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "title")
        val title: String,
        @ColumnInfo(name = "departmentId")
        val departmentId: Int,
        @ColumnInfo(name = "departmentName")
        val departmentName: String,
        @ColumnInfo(name = "preview")
        val preview: String,
        @ColumnInfo(name = "tags")
        val tags: String,
        @ColumnInfo(name = "isDeleted")
        val isDeleted: Int
        // true : 1, false : 0
)
