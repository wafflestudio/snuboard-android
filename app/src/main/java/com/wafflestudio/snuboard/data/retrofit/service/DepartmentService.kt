package com.wafflestudio.snuboard.data.retrofit.service

import com.wafflestudio.snuboard.data.retrofit.dto.DepartmentDto
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface DepartmentService {
    @GET("departments/")
    fun getDepartments(): Observable<List<DepartmentDto>>

    @GET("departments/{department_id}/")
    fun getDepartmentById(
        @Path("department_id") departmentId: Int
    ): Single<DepartmentDto>


}