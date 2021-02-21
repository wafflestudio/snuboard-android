package com.wafflestudio.snuboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor() : ViewModel() {
    private val _isDrawerOpen = MutableLiveData(false)
    val isDrawerOpen: LiveData<Boolean>
        get() = _isDrawerOpen

    fun setDrawer(bool: Boolean) {
        _isDrawerOpen.value = bool
    }
}