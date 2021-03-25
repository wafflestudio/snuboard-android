package com.wafflestudio.snuboard.data.repository

import com.wafflestudio.snuboard.data.retrofit.service.NoticeService
import com.wafflestudio.snuboard.domain.translater.NoticeMapper
import com.wafflestudio.snuboard.utils.parseErrorResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface NoticeRepository {

    fun getNoticesOfFollow(limit: Int, cursor: String?): Single<Any>

    fun getNoticesOfScrap(limit: Int, cursor: String?): Single<Any>

    fun getNoticeById(noticeId: Int): Single<Any>

    fun deleteNoticeScrap(noticeId: Int): Single<Any>

    fun postNoticeScrap(noticeId: Int): Single<Any>
}

@Singleton
class NoticeRepositoryImpl
@Inject
constructor(
        private val noticeService: NoticeService,
        private val noticeMapper: NoticeMapper,
) : NoticeRepository {

    override fun getNoticesOfFollow(limit: Int, cursor: String?): Single<Any> {
        return noticeService.getNoticesOfFollow(limit, cursor)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map noticeMapper.mapFromListNoticeDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun getNoticesOfScrap(limit: Int, cursor: String?): Single<Any> {
        return noticeService.getNoticesOfScrap(limit, cursor)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map noticeMapper.mapFromListNoticeDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun getNoticeById(noticeId: Int): Single<Any> {
        return noticeService.getNoticeById(noticeId)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map noticeMapper.mapToNoticeDetailFromNoticeDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun deleteNoticeScrap(noticeId: Int): Single<Any> {
        return noticeService.deleteNoticeScrap(noticeId)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map noticeMapper.mapToNoticeDetailFromNoticeDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

    override fun postNoticeScrap(noticeId: Int): Single<Any> {
        return noticeService.postNoticeScrap(noticeId)
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.isSuccessful) {
                        it.body()?.let { it1 ->
                            return@map noticeMapper.mapToNoticeDetailFromNoticeDto(it1)
                        }
                    } else
                        return@map parseErrorResponse(it.errorBody()!!)
                }
    }

}
