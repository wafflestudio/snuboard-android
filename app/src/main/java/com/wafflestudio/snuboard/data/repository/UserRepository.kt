package com.wafflestudio.snuboard.data.repository

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.data.retrofit.service.UserService
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.domain.translater.UserMapper
import com.wafflestudio.snuboard.utils.parseErrorResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {

    fun login(username: String, password: String): Single<Any>

    fun signUp(username: String, password: String, email: String): Single<Any>

    fun getUserMe(): Single<Any>

    fun deleteUserMe(): Single<Any>

    fun subscribeToMyFCMTopics(fcmToken: String): Single<Any>

    fun unsubscribeFromMyFCMTopics(fcmToken: String): Single<Any>
}

@Singleton
class UserRepositoryImpl
@Inject
constructor(
        private val userService: UserService,
        private val userMapper: UserMapper,
        @ApplicationContext appContext: Context
) : UserRepository {

    private val pref = PreferenceManager.getDefaultSharedPreferences(
            appContext
    )

    override fun login(username: String, password: String): Single<Any> {
        return userService.authWithPassword("password", username, password)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    it.body()?.let { it1 ->
                        pref.edit {
                            putString(SharedPreferenceConst.REFRESH_TOKEN_KEY, it1.refreshToken)
                            putString(SharedPreferenceConst.ACCESS_TOKEN_KEY, it1.accessToken)
                        }
                        return@map userMapper.mapFromUserTokenDto(it1)
                    }
                } else
                    return@map parseErrorResponse(it.errorBody()!!)
            }
    }

    override fun signUp(username: String, password: String, email: String): Single<Any> {
        return userService.postUser(username, password, email)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            pref.edit {
                                putString(SharedPreferenceConst.REFRESH_TOKEN_KEY, it1.refreshToken)
                                putString(SharedPreferenceConst.ACCESS_TOKEN_KEY, it1.accessToken)
                            }
                            return@map userMapper.mapFromUserTokenDto(it1)
                        }
                } else
                    return@map parseErrorResponse(it.errorBody()!!)
            }
    }

    override fun getUserMe(): Single<Any> {
        return userService.getUserMe()
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map userMapper.mapFromUserDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun deleteUserMe(): Single<Any> {
        return userService.deleteUserMe()
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map userMapper.mapFromUserDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun subscribeToMyFCMTopics(fcmToken: String): Single<Any> {
        return userService.subscribeToMyFCMTopics(fcmToken)
            .subscribeOn(Schedulers.io())
            .map {
                return@map it.isSuccessful
            }
    }

    override fun unsubscribeFromMyFCMTopics(fcmToken: String): Single<Any> {
        return userService.unsubscribeFromMyFCMTopics(fcmToken)
            .subscribeOn(Schedulers.io())
            .map {
                return@map it.isSuccessful
            }
    }

}
