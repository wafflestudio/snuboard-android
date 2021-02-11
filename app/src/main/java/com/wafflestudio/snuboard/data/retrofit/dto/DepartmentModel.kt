package com.wafflestudio.snuboard.data.retrofit.dto

data class DepartmentDto(
        val id: Int,
        val name: String,
        val tags: List<String>,
        val follow: List<String>
)