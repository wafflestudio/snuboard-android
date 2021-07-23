package com.wafflestudio.snuboard.domain.model

data class User(
        val id: Int,
        val username: String,
        val email: String,
        val keywords: List<String>
)
