package com.wafflestudio.snuboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.User
import com.wafflestudio.snuboard.domain.usecase.NotifyUseCase
import com.wafflestudio.snuboard.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
        private val notifyUseCase: NotifyUseCase,
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

    fun eraseNotificationList(postWork: () -> Unit = {}) {
        notifyUseCase
                .deleteAllNoticeNotis()
                .subscribe({
                    postWork()
                }, {
                    Timber.e(it)
                })
    }

    fun navigateToAuthActivity() {
        _navigateToAuthActivity.value = Event(Unit)
    }

}
