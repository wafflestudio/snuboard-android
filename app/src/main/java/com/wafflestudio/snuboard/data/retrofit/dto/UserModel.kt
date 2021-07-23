package com.wafflestudio.snuboard.data.retrofit.dto

import com.google.gson.annotations.SerializedName

data class UserTokenDto(
        val id: Int,
        val username: String,
        val email: String,
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("refresh_token")
        val refreshToken: String,
        val keywords: List<String>
)

data class UserDto(
        val id: Int,
        val username: String,
        val email: String,
        val keywords: List<String>
)

data class PatchUserDto(
        val email: String? = null,
        val password: String? = null
)
