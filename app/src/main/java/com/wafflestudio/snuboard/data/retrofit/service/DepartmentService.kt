package com.wafflestudio.snuboard.data.retrofit.service

import com.wafflestudio.snuboard.data.retrofit.dto.DepartmentDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface DepartmentService {
    @GET("departments/")
    fun getDepartments(): Single<List<DepartmentDto>>

    @GET("departments/{department_id}/")
    fun getDepartmentById(
            @Path("department_id") departmentId: Int
    ): Single<DepartmentDto>

    @FormUrlEncoded
    @DELETE("departments/{department_id}/follow/")
    fun deleteFollowOfDepartment(
            @Path("department_id") departmentId: Int,
            @Field("follow") follow: String
    ): Single<DepartmentDto>

    @FormUrlEncoded
    @POST("departments/{department_id}/follow/")
    fun postFollowOfDepartment(
            @Path("department_id") departmentId: Int,
            @Field("follow") follow: String
    ): Single<DepartmentDto>
}