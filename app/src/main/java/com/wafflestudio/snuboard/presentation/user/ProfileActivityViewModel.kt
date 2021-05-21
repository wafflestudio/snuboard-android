package com.wafflestudio.snuboard.presentation.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileActivityViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
}
