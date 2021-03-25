package com.wafflestudio.snuboard.data.retrofit.service

import com.wafflestudio.snuboard.data.retrofit.dto.NoticeDto
import com.wafflestudio.snuboard.data.retrofit.dto.NoticeListDto
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
    ): Single<Response<NoticeListDto>>

    @GET("notices/department/{department_id}/search/")
    fun getNoticesOfDepartmentIdSearch(
            @Path("department_id") departmentId: Int,
            @Query("keyword") keyword: String,
            @Query("limit") limit: Int,
            @Query("cursor") cursor: String?,
            @Query("content") content: Boolean?,
            @Query("title") title: Boolean?,
            @Query("pinned") pinned: Boolean?,
            @Query("tags") tags: String?
    ): Single<Response<NoticeListDto>>

    @GET("notices/follow/")
    fun getNoticesOfFollow(
            @Query("limit") limit: Int,
            @Query("cursor") cursor: String?
    ): Single<Response<NoticeListDto>>

    @GET("notices/follow/search/")
    fun getNoticesOfFollowSearch(
            @Query("keyword") keyword: String,
            @Query("limit") limit: Int,
            @Query("cursor") cursor: String?,
            @Query("content") content: Boolean?,
            @Query("title") title: Boolean?,
    ): Single<Response<NoticeListDto>>

    @GET("notices/scrap/")
    fun getNoticesOfScrap(
            @Query("limit") limit: Int,
            @Query("cursor") cursor: String?
    ): Single<Response<NoticeListDto>>

    @GET("notices/{notice_id}/")
    fun getNoticeById(
            @Path("notice_id") noticeId: Int
    ): Single<Response<NoticeDto>>

    @DELETE("notices/{notice_id}/scrap/")
    fun deleteNoticeScrap(
            @Path("notice_id") noticeId: Int
    ): Single<Response<NoticeDto>>

    @POST("notices/{notice_id}/scrap/")
    fun postNoticeScrap(
            @Path("notice_id") noticeId: Int
    ): Single<Response<NoticeDto>>
}
