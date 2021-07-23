package com.wafflestudio.snuboard.data.retrofit.dto

import com.google.gson.annotations.SerializedName

data class NoticeListDto(
        val notices: List<NoticeSimpleDto>,
        @SerializedName("next_cursor")
        val nextCursor: String
)

data class NoticeSimpleDto(
        val id: Int,
        @SerializedName("department_name")
        val departmentName: String,
        @SerializedName("department_id")
        val departmentId: Int,
        val title: String,
        val preview: String,
        @SerializedName("created_at")
        val createdAt: String,
        val tags: List<String>,
        @SerializedName("is_pinned")
        val isPinned: Boolean,
        val link: String,
        @SerializedName("is_scrapped")
        val isScrapped: Boolean
)

data class NoticeDto(
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
        val files: List<NoticeFileDto>,
        @SerializedName("is_scrapped")
        val isScrapped: Boolean,
        val style: String
)

data class NoticeFileDto(
        val id: Int,
        val name: String,
        val link: String
)
