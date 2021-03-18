package com.wafflestudio.snuboard.presentation.notice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityNoticeSearchBinding

class NoticeSearchActivity : AppCompatActivity() {

    private val binding: ActivityNoticeSearchBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_notice_search
        ) as ActivityNoticeSearchBinding
    }

    private val noticeSearchActivityViewModel: NoticeSearchActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            lifecycleOwner = this@NoticeSearchActivity
            viewModel = noticeSearchActivityViewModel

            setSupportActionBar(toolBar)
            supportActionBar!!.apply {
                setDisplayShowTitleEnabled(false)
            }

            searchText.postDelayed({
                showSoftKeyboard(searchText)
            }, 200)

        }
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.slide_to_right)
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, NoticeSearchActivity::class.java)
    }
}