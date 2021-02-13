package com.wafflestudio.snuboard.data.retrofit.dto

data class NoticeList(
        val notices: List<Notice>,
        val nextCursor: String
)

data class Notice(
        val id: Int,
        val departmentName: String,
        val departmentId: Int,
        val title: String,
        val preview: String,
        val content: String,
        val createdAt: String,
        val tags: List<String>,
        val isPinned: Boolean,
        val link: String,
        val files: List<NoticeFile>
)

data class NoticeFile(
        val id: Int,
        val name: String,
        val link: String
)