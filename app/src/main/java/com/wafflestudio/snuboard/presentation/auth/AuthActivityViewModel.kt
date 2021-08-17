package com.wafflestudio.snuboard.presentation.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.domain.model.User
import com.wafflestudio.snuboard.domain.usecase.FCMTopicUseCase
import com.wafflestudio.snuboard.domain.usecase.LoginUseCase
import com.wafflestudio.snuboard.domain.usecase.SignUpUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent.triggerToast
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthActivityViewModel
@Inject
constructor(
        private val signUpUseCase: SignUpUseCase,
        private val loginUseCase: LoginUseCase,
        private val fcmTopicUseCase: FCMTopicUseCase,
        @ApplicationContext appContext: Context,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pref = PreferenceManager.getDefaultSharedPreferences(
            appContext
    )

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()

    val usernameLoginFragment = MutableLiveData<String>()
    val passwordLoginFragment = MutableLiveData<String>()

    val usernameSignUpFragment = MutableLiveData<String>()
    val passwordSignUpFragment = MutableLiveData<String>()
    val confirmPasswordSignUpFragment = MutableLiveData<String>()
    val emailSignUpFragment = MutableLiveData<String>()

    val navigateToMainActivity: LiveData<Event<Unit>>
        get() = _navigateToMainActivity


    fun signUp() {
        when {
            usernameSignUpFragment.value == null ->
                triggerToast.value = Event("Username이 비어있습니다.")
            emailSignUpFragment.value == null ->
                triggerToast.value = Event("Email이 비어있습니다.")
            !isEmailValid(emailSignUpFragment.value!!) ->
                triggerToast.value = Event("이메일이 옳지 않은 형식입니다.")
            passwordSignUpFragment.value == null ->
                triggerToast.value = Event("Password가 비어있습니다.")
            confirmPasswordSignUpFragment.value == null ->
                triggerToast.value = Event("Confirm Password가 비어있습니다.")
            passwordSignUpFragment.value != confirmPasswordSignUpFragment.value ->
                triggerToast.value = Event("비밀번호와 비밀번호 확인이 다릅니다.")
            else ->
                signUpUseCase
                        .signUp(
                                usernameSignUpFragment.value!!,
                                passwordSignUpFragment.value!!,
                                emailSignUpFragment.value!!
                        )
                        .subscribe({
                            when (it) {
                                is User -> {
                                    _navigateToMainActivity.value = Event(Unit)
                                }
                                is ErrorResponse -> {
                                    triggerToast.value = Event(it.message)
                                    Timber.e(it.message)
                                }
                            }
                        }, {
                            Timber.e(it)
                        })
        }
    }

    fun login() {
        when {
            usernameLoginFragment.value == null ->
                triggerToast.value = Event("Username이 비어있습니다.")
            passwordLoginFragment.value == null ->
                triggerToast.value = Event("Password가 비어있습니다.")
            else ->
                loginUseCase
                    .login(
                            usernameLoginFragment.value!!,
                            passwordLoginFragment.value!!
                    )
                    .subscribe({
                        when (it) {
                            is User -> {
                                fcmTopicUseCase.subscribeAll().continueWith {
                                    if (it.isSuccessful)
                                        _navigateToMainActivity.value = Event(Unit)
                                    else
                                        triggerToast.value = Event("심각한 오류가 발생했습니다. 앱을 재설치해주세요.")
                                }
                            }
                            is ErrorResponse -> {
                                triggerToast.value = Event(it.message)
                                Timber.e(it.message)
                            }
                        }
                    }, {
                        Timber.e(it)
                    })
        }
    }

    fun checkToken() {
        pref.getString(SharedPreferenceConst.ACCESS_TOKEN_KEY, null)
                ?.run {
                    _navigateToMainActivity.value = Event(Unit)
                }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
