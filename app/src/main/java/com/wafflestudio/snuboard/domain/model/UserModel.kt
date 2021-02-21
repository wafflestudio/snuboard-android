package com.wafflestudio.snuboard.domain.model

data class User(
    val id: Int,
    val username: String,
    val nickname: String,
    val keywords: List<String>
)
