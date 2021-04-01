package com.wafflestudio.snuboard.presentation.notice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityNoticeSearchBinding
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        SingleEvent.triggerToast.observe(this@NoticeSearchActivity) {
            it.getContentIfNotHandled()?.let { it1 ->
                Toast.makeText(this@NoticeSearchActivity, it1, Toast.LENGTH_SHORT).show()
            }
        }
        binding.run {
            lifecycleOwner = this@NoticeSearchActivity
            viewModel = noticeSearchActivityViewModel

            setSupportActionBar(toolBar)
            supportActionBar!!.apply {
                setDisplayShowTitleEnabled(false)
            }

            searchText.apply {
                postDelayed({
                    showSoftKeyboard(searchText)
                }, 200)
                setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            hideSoftKeyboard(searchText)
                            noticeSearchActivityViewModel.apply {
                                cleanCursor()
                                clearNotices()
                                getNotices()
                            }
                            searchText.clearFocus()
                            return true
                        }
                        return false
                    }
                })
            }
            cancelButton.setOnClickListener {
                finish()
            }
            eraseButton.setOnClickListener {
                noticeSearchActivityViewModel.cleanText()
                searchText.requestFocus()
                showSoftKeyboard(searchText)
            }

            recyclerView.run {
                val myLayoutManager = LinearLayoutManager(this@NoticeSearchActivity)
                layoutManager = myLayoutManager
                adapter = NoticeListAdapter(
                        HeartClickListener {
                            noticeSearchActivityViewModel.toggleSavedNotice(it)
                        }
                )
                clearOnScrollListeners()
                addOnScrollListener(NoticeInfiniteScrollListener(myLayoutManager) {
                    noticeSearchActivityViewModel.getNotices()
                })
            }
        }
        noticeSearchActivityViewModel.apply {
            updateNotice.observe(this@NoticeSearchActivity) {
                observeUpdateNotice(it)
            }
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

    private fun hideSoftKeyboard(view: View) {
        val imm = this@NoticeSearchActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, NoticeSearchActivity::class.java)
    }
}
