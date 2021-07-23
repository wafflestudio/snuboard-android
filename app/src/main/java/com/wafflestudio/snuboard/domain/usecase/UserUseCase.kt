package com.wafflestudio.snuboard.domain.usecase

import com.wafflestudio.snuboard.data.repository.UserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUseCase
@Inject
constructor(
    private val userRepository: UserRepository
) {
    fun signUp(username: String, password: String, email: String): Single<Any> {
        return userRepository
                .signUp(username, password, email)
                .observeOn(AndroidSchedulers.mainThread())
    }
}

@Singleton
class LoginUseCase
@Inject
constructor(
    private val userRepository: UserRepository
) {
    fun login(username: String, password: String): Single<Any> {
        return userRepository
            .login(username, password)
            .observeOn(AndroidSchedulers.mainThread())
    }
}

@Singleton
class GetMyInfoUseCase
@Inject
constructor(
    private val userRepository: UserRepository
) {
    fun getMyInfo(): Single<Any> {
        return userRepository
            .getUserMe()
            .observeOn(AndroidSchedulers.mainThread())
    }
}
