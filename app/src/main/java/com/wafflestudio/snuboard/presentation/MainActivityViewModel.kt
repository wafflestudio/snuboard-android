package com.wafflestudio.snuboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.wafflestudio.snuboard.domain.model.User
import com.wafflestudio.snuboard.domain.usecase.FCMTopicUseCase
import com.wafflestudio.snuboard.domain.usecase.GetMyInfoUseCase
import com.wafflestudio.snuboard.domain.usecase.SignOutUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
        private val getMyInfoUseCase: GetMyInfoUseCase,
        private val signOutUseCase: SignOutUseCase,
        private val fcmTopicUseCase: FCMTopicUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _isDrawerOpen = MutableLiveData(false)
    private val _myInfo = MutableLiveData<User>()
    private val _navigateToAuthActivity = MutableLiveData<Event<Unit>>()
    private val _signOutNavigate = MutableLiveData<Event<Unit>>()

    val isDrawerOpen: LiveData<Boolean>
        get() = _isDrawerOpen
    val myInfo: LiveData<User>
        get() = _myInfo
    val navigateToAuthActivity: LiveData<Event<Unit>>
        get() = _navigateToAuthActivity
    val signOutNavigate: LiveData<Event<Unit>>
        get() = _signOutNavigate

    fun setDrawer(bool: Boolean) {
        _isDrawerOpen.value = bool
    }

    fun getMyInfo() {
        getMyInfoUseCase
                .getMyInfo()
                .subscribe({
                    when (it) {
                        is User -> {
                            _myInfo.value = it
                        }
                        is ErrorResponse -> {
                            SingleEvent.triggerToast.value = Event(it.message)
                            Timber.e(it.message)
                            if (it.statusCode == 401) {
                                _navigateToAuthActivity.value = Event(Unit)
                            }
                        }
                    }
                }, {
                    Timber.e(it)
                })
    }

    fun signOut() {
        signOutUseCase
                .signOut()
                .subscribe({
                    when (it) {
                        is User -> {
                            fcmTopicUseCase.unsubscribeAll().continueWith {
                                if(it.isSuccessful)
                                    _navigateToAuthActivity.value = Event(Unit)
                                else {
                                    SingleEvent.triggerToast.value = Event(
                                        "심각한 오류가 발생했습니다. 앱을 재설치해주세요."
                                    )
                                }
                            }

                        }
                        is ErrorResponse -> {
                            SingleEvent.triggerToast.value = Event(it.message)
                            Timber.e(it.message)
                            if (it.statusCode == 401) {
                                _navigateToAuthActivity.value = Event(Unit)
                            }
                        }
                    }
                }, {
                    Timber.e(it)
                })
    }

    fun unSubscribe(): Task<Void> {
        return fcmTopicUseCase.unsubscribeAll()
    }

}
