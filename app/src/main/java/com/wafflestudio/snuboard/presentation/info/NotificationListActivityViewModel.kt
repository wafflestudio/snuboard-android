package com.wafflestudio.snuboard.presentation.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationListActivityViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isNotificationActive = MutableLiveData<Boolean>(true)
    val isNotificationActive: LiveData<Boolean>
        get() = _isNotificationActive

    fun toggleNotification() {
        _isNotificationActive.value?.let {
            _isNotificationActive.value = !it
        }
    }
}
