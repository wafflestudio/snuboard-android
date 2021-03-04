package com.wafflestudio.snuboard.domain.model

data class NoticeList(
        val notices: List<Notice>,
        val nextCursor: String
)

data class Notice(
        val id: Int,
        val departmentName: String,
        val departmentId: Int,
        val departmentColor: DepartmentColor,
        val title: String,
        val preview: String,
        val content: String,
        val createdAtYYMMDD: String,
        val createdAtYYMMDDhhmm: String,
        val tags: List<String>,
        val isPinned: Boolean,
        val link: String,
        val files: List<NoticeFile>,
        val isScrapped: Boolean
)

data class NoticeFile(
        val id: Int,
        val name: String,
        val link: String
)
