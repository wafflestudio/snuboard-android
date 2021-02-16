package com.wafflestudio.snuboard.presentation.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.Notice

class NoticeFeedFragmentViewModel : ViewModel() {
    private val _notices = MutableLiveData<List<Notice>>()
    val notices: LiveData<List<Notice>>
        get() = _notices
}