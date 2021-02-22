package com.wafflestudio.snuboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.User
import com.wafflestudio.snuboard.domain.usecase.GetMyInfoUseCase
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _isDrawerOpen = MutableLiveData(false)
    private val _myInfo = MutableLiveData<User>()

    val isDrawerOpen: LiveData<Boolean>
        get() = _isDrawerOpen
    val myInfo: LiveData<User>
        get() = _myInfo

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
                    }
                }
            }, {
                Timber.e(it)
            })
    }

}