package com.wafflestudio.snuboard.data.retrofit.service

import com.wafflestudio.snuboard.data.retrofit.dto.PatchUserDto
import com.wafflestudio.snuboard.data.retrofit.dto.UserDto
import com.wafflestudio.snuboard.data.retrofit.dto.UserTokenDto
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @FormUrlEncoded
    @POST("users/")
    fun postUser(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("email") email: String
    ): Single<Response<UserTokenDto>>

    @FormUrlEncoded
    @POST("users/auth/token/")
    fun authWithPassword(
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<Response<UserTokenDto>>

    @FormUrlEncoded
    @POST("users/auth/token/")
    fun authWithToken(
            @Field("grant_type") grantType: String,
            @Header("Authorization") refreshToken: String
    ): Call<UserTokenDto>

    @GET("users/me/")
    fun getUserMe(): Single<Response<UserDto>>

    @PATCH("users/me/")
    fun patchUserMe(
        @Body body: PatchUserDto
    ): Single<Response<UserDto>>

    @FormUrlEncoded
    @DELETE("users/me/keyword/")
    fun deleteUserKeyword(
            @Field("keyword") keyword: String,
    ): Single<Response<UserDto>>

    @FormUrlEncoded
    @POST("users/me/keyword/")
    fun postUserKeyword(
            @Field("keyword") keyword: String,
    ): Single<Response<UserDto>>

    @DELETE("users/me/")
    fun deleteUserMe(): Single<Response<UserDto>>
}
