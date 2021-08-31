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
    fun signUp(token: String): Single<Any> {
        return userRepository
                .signUp(token)
                .observeOn(AndroidSchedulers.mainThread())
    }
}
