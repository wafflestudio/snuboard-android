package com.wafflestudio.snuboard.domain.model

data class NoticeList(
        val notices: List<Notice>,
        val nextCursor: String
)

// Used in bindNoticeTagItems in BindingUtils.kt
sealed class NoticeInterface(
        open val id: Int,
        open val departmentName: String,
        open val departmentColor: DepartmentColor,
        open val tags: List<String>
)

data class Notice(
        override val id: Int,
        override val departmentName: String,
        val departmentId: Int,
        override val departmentColor: DepartmentColor,
        val title: String,
        val preview: String,
        val createdAtYYMMDD: String,
        override val tags: List<String>,
        val isPinned: Boolean,
        val isScrapped: Boolean
) : NoticeInterface(id, departmentName, departmentColor, tags)

data class NoticeDetail(
        override val id: Int,
        override val departmentName: String,
        val departmentId: Int,
        override val departmentColor: DepartmentColor,
        val title: String,
        val preview: String,
        val content: String,
        val createdAtYYMMDD: String,
        val createdAtYYMMDDhhmm: String,
        override val tags: List<String>,
        val isPinned: Boolean,
        val link: String,
        val files: List<NoticeFile>,
        val isScrapped: Boolean,
        val style: String
) : NoticeInterface(id, departmentName, departmentColor, tags)

data class NoticeFile(
        val id: Int,
        val name: String,
        val link: String
)
