package com.wafflestudio.snuboard.data.retrofit.service

import com.wafflestudio.snuboard.data.retrofit.dto.UserTokenDto
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @FormUrlEncoded
    @POST("users/")
    fun postUser(
            @Field("token") token: String
    ): Single<Response<UserTokenDto>>

    @FormUrlEncoded
    @POST("users/auth/token/")
    fun authWithToken(
            @Header("Authorization") refreshToken: String
    ): Call<UserTokenDto>

    @FormUrlEncoded
    @POST("users/me/fcm/topics/")
    fun subscribeToMyFCMTopics(
        @Field("token") fcmToken: String,
    ): Single<Response<Void>>

    @FormUrlEncoded
    @DELETE("users/me/fcm/topics/")
    fun unsubscribeFromMyFCMTopics(
        @Field("token") fcmToken: String,
    ): Single<Response<Void>>
}
