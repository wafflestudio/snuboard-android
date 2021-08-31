package com.wafflestudio.snuboard.data.retrofit.dto

import com.google.gson.annotations.SerializedName

data class UserTokenDto(
        val id: Int,
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("refresh_token")
        val refreshToken: String,
)
