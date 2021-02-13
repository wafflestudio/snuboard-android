package com.wafflestudio.snuboard.data.retrofit.service

import com.wafflestudio.snuboard.data.retrofit.dto.NoticeList
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.*

interface NoticeService {
    @GET("notices/department/{department_id}/")
    fun getNoticesOfDepartment(
            @Path("department_id") departmentId: Int,
            @Query("limit") limit: Int,
            @Query("cursor") cursor: String?,
            @Query("pinned") pinned: Boolean?,
            @Query("tags") tags: String?
    ): Single<Response<NoticeList>>

    @GET("notices/follow/")
    fun getNoticesOfFollow(
            @Query("limit") limit: Int,
            @Query("cursor") cursor: String?
    ): Single<Response<NoticeList>>

    @GET("notices/scrap/")
    fun getNoticesOfScrap(
            @Query("limit") limit: Int,
            @Query("cursor") cursor: String?
    ): Single<Response<NoticeList>>

    @GET("notices/{notice_id}/")
    fun getNoticeById(
            @Path("notice_id") noticeId: Int
    )

    @DELETE("notices/{notice_id}/scrap/")
    fun deleteNoticeScrap(
            @Path("notice_id") noticeId: Int
    )

    @POST("notices/{notice_id}/scrap/")
    fun postNoticeScrap(
            @Path("notice_id") noticeId: Int
    )
}