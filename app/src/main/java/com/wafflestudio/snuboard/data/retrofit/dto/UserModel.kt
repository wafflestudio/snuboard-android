package com.wafflestudio.snuboard.data.retrofit.dto

data class UserTokenDto(
    val id: Int,
    val username: String,
    val nickname: String,
    val access_token: String,
    val refresh_token: String,
    val keywords: List<String>
)

