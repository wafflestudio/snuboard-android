package com.wafflestudio.snuboard.data.retrofit.service

import com.wafflestudio.snuboard.data.retrofit.dto.UserTokenDto
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface UserService {
    @FormUrlEncoded
    @GET("/users/auth/token/")
    fun refreshWithToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Call<UserTokenDto>
}