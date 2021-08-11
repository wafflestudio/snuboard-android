package com.wafflestudio.snuboard.presentation.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.data.repository.NoticeNotiRepository
import com.wafflestudio.snuboard.data.room.NoticeNoti
import com.wafflestudio.snuboard.domain.usecase.NotifyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

@HiltViewModel
class NotificationListActivityViewModel
@Inject
constructor(
        private val noticeNotiRepository: NoticeNotiRepository,
        private val notifyUseCase: NotifyUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isNotificationActive = MutableLiveData<Boolean>(null)
    val isNotificationActive: LiveData<Boolean>
        get() = _isNotificationActive

    val allNoticeNotis: LiveData<List<NoticeNoti>> = noticeNotiRepository.getAllNoticeNotis()

    fun getNotification() {
        _isNotificationActive.value = noticeNotiRepository.getIsNotificationActive()
    }

    fun toggleNotification() {
        _isNotificationActive.value?.let {
            noticeNotiRepository.setIsNotificationActive(!it)
            _isNotificationActive.value = !it
        }
    }

    fun deleteNoticeNoti(id: Int): Completable {
        return notifyUseCase.deleteNoticeNoti(id)
    }

    fun addNoticeNoti(
            id: Int,
            title: String,
            departmentId: Int,
            departmentName: String,
            preview: String,
            tags: String,
            postWork: () -> Unit = {}
    ): Completable {
        return notifyUseCase.addNoticeNoti(
                id, title, departmentId, departmentName, preview, tags, postWork
        )
    }

}
