package com.wafflestudio.snuboard.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wafflestudio.snuboard.data.repository.DepartmentRepository
import com.wafflestudio.snuboard.domain.model.CollegeDepartment
import com.wafflestudio.snuboard.domain.model.Department
import com.wafflestudio.snuboard.domain.model.FollowingDepartment
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassifyDepartmentUseCase
@Inject
constructor(
        private val departmentRepository: DepartmentRepository
) {

    private val _updateDepartments = MutableLiveData<Event<Unit>>()
    val updateDepartments: LiveData<Event<Unit>>
        get() = _updateDepartments

    fun classifyDepartments(): Observable<Any> {
        return departmentRepository
                .getDepartments()
                .toObservable()
                .flatMap {
                    when (it) {
                        is List<*> -> {
                            return@flatMap Observable.fromIterable(it).concatMap { it1 ->
                                when {
                                    it1 is Department && it1.follow.isEmpty() ->
                                        Observable.just(
                                                CollegeDepartment.fromDepartmentIdName(it1.id, it1.name)
                                        )
                                    it1 is Department && it1.follow.isNotEmpty() ->
                                        Observable.just(
                                                CollegeDepartment.fromDepartmentIdName(it1.id, it1.name),
                                                FollowingDepartment(it1.id, it1.name, it1.follow, it1.departmentColor)
                                        )
                                    else ->
                                        throw Error("Unexpected type from DepartmentMapper")
                                }
                            }
                        }
                        is ErrorResponse ->
                            return@flatMap Observable.just(it)
                        else ->
                            throw Error("Unexpected type for getDepartments.")
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun updateDepartments() {
        _updateDepartments.value = Event(Unit)
    }
}

@Singleton
class GetDepartmentInfoUseCase
@Inject
constructor(
        private val departmentRepository: DepartmentRepository
) {
    fun getDepartmentInfo(departmentId: Int): Single<Any> {
        return departmentRepository
                .getDepartmentById(departmentId)
                .observeOn(AndroidSchedulers.mainThread())
    }
}