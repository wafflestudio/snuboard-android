package com.wafflestudio.snuboard.presentation.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.domain.model.User
import com.wafflestudio.snuboard.domain.usecase.SignUpUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent.triggerToast
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthActivityViewModel
@Inject
constructor(
        private val signUpUseCase: SignUpUseCase,
        @ApplicationContext appContext: Context,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pref = PreferenceManager.getDefaultSharedPreferences(
            appContext
    )

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()

    val navigateToMainActivity: LiveData<Event<Unit>>
        get() = _navigateToMainActivity


    fun signUp() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            val token = task.result
            if (token != null) {
                signUpUseCase
                        .signUp(token)
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
        })
    }

    fun checkToken() {
        pref.getString(SharedPreferenceConst.ACCESS_TOKEN_KEY, null)
                ?.run {
                    _navigateToMainActivity.value = Event(Unit)
                }
    }
}
