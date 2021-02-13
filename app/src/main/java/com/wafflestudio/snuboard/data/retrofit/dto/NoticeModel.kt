package com.wafflestudio.snuboard.data.retrofit.dto

import com.google.gson.annotations.SerializedName

data class NoticeList(
        val notices: List<Notice>,
        @SerializedName("next_cursor")
        val nextCursor: String
)

data class Notice(
        val id: Int,
        @SerializedName("department_name")
        val departmentName: String,
        @SerializedName("department_id")
        val departmentId: Int,
        val title: String,
        val preview: String,
        val content: String,
        @SerializedName("created_at")
        val createdAt: String,
        val tags: List<String>,
        @SerializedName("is_pinned")
        val isPinned: Boolean,
        val link: String,
        val files: List<NoticeFile>,
        @SerializedName("is_scrapped")
        val isScrapped: Boolean
)

data class NoticeFile(
        val id: Int,
        val name: String,
        val link: String
)