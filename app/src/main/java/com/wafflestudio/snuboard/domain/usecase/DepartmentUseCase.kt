package com.wafflestudio.snuboard.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wafflestudio.snuboard.data.repository.DepartmentRepository
import com.wafflestudio.snuboard.domain.model.*
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
                                                CollegeDepartment(it1.id, it1.name, it1.college)
                                        )
                                    it1 is Department && it1.follow.isNotEmpty() ->
                                        Observable.just(
                                                CollegeDepartment(it1.id, it1.name, it1.college),
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
class GetTagDepartmentInfoUseCase
@Inject
constructor(
        private val departmentRepository: DepartmentRepository
) {
    fun getTagDepartmentInfo(departmentId: Int): Single<Any> {
        return departmentRepository
                .getDepartmentById(departmentId)
                .map {
                    when (it) {
                        is Department -> {
                            val tmpTags = it.tags.map { it1 ->
                                if (it1 in it.follow) {
                                    Tag(it1, DepartmentColor.TAG_SELECTED_COLOR)
                                } else {
                                    Tag(it1, DepartmentColor.TAG_COLOR)
                                }
                            }
                            return@map TagDepartment(it.id, it.name, it.link, tmpTags, it.departmentColor)
                        }
                        is ErrorResponse ->
                            return@map it
                        else ->
                            throw Error("Unexpected type from DepartmentMapper")
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
    }
}

@Singleton
class PostFollowingTagUseCase
@Inject
constructor(
        private val departmentRepository: DepartmentRepository
) {
    fun postFollowingTag(departmentId: Int, tagContent: String): Single<Any> {
        return departmentRepository
                .postFollowOfDepartment(departmentId, tagContent)
                .map {
                    when (it) {
                        is Department -> {
                            val tmpTags = it.tags.map { it1 ->
                                if (it1 in it.follow) {
                                    Tag(it1, DepartmentColor.TAG_SELECTED_COLOR)
                                } else {
                                    Tag(it1, DepartmentColor.TAG_COLOR)
                                }
                            }
                            return@map TagDepartment(it.id, it.name, it.link, tmpTags, it.departmentColor)
                        }
                        is ErrorResponse ->
                            return@map it
                        else ->
                            throw Error("Unexpected type from DepartmentMapper")
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())


    }
}

@Singleton
class DeleteFollowingTagUseCase
@Inject
constructor(
        private val departmentRepository: DepartmentRepository
) {
    fun deleteFollowingTag(departmentId: Int, tagContent: String): Single<Any> {
        return departmentRepository
                .deleteFollowOfDepartment(departmentId, tagContent)
                .map {
                    when (it) {
                        is Department -> {
                            val tmpTags = it.tags.map { it1 ->
                                if (it1 in it.follow) {
                                    Tag(it1, DepartmentColor.TAG_SELECTED_COLOR)
                                } else {
                                    Tag(it1, DepartmentColor.TAG_COLOR)
                                }
                            }
                            return@map TagDepartment(it.id, it.name, it.link, tmpTags, it.departmentColor)
                        }
                        is ErrorResponse ->
                            return@map it
                        else ->
                            throw Error("Unexpected type from DepartmentMapper")
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
    }
}
