package com.wafflestudio.snuboard.presentation.notice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityNoticeDetailBinding

class NoticeDetailActivity : AppCompatActivity() {

    private val binding: ActivityNoticeDetailBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_notice_detail
        ) as ActivityNoticeDetailBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {

        }
    }

    companion object {
        fun intent(context: Context, noticeId: Int) =
            Intent(context, NoticeDetailActivity::class.java)
                .putExtra(EXTRA_NOTICE_ID, noticeId)

        private const val EXTRA_NOTICE_ID = "extra_notice_id"
    }
}