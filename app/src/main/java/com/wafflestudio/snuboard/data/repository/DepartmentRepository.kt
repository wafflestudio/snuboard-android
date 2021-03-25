package com.wafflestudio.snuboard.data.repository

import com.wafflestudio.snuboard.data.retrofit.service.DepartmentService
import com.wafflestudio.snuboard.domain.translater.DepartmentMapper
import com.wafflestudio.snuboard.utils.parseErrorResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface DepartmentRepository {
    fun getDepartments(): Single<Any>

    fun getDepartmentById(departmentId: Int): Single<Any>

    fun deleteFollowOfDepartment(departmentId: Int, follow: String): Single<Any>

    fun postFollowOfDepartment(departmentId: Int, follow: String): Single<Any>
}

@Singleton
class DepartmentRepositoryImpl
@Inject
constructor(
        private val departmentService: DepartmentService,
        private val departmentMapper: DepartmentMapper
) : DepartmentRepository {
    override fun getDepartments(): Single<Any> {
        return departmentService.getDepartments()
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map departmentMapper.mapFromListDepartmentDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun getDepartmentById(departmentId: Int): Single<Any> {
        return departmentService.getDepartmentById(departmentId)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map departmentMapper.mapFromDepartmentDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun deleteFollowOfDepartment(departmentId: Int, follow: String): Single<Any> {
        return departmentService.deleteFollowOfDepartment(departmentId, follow)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map departmentMapper.mapFromDepartmentDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun postFollowOfDepartment(departmentId: Int, follow: String): Single<Any> {
        return departmentService.postFollowOfDepartment(departmentId, follow)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map departmentMapper.mapFromDepartmentDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

}
