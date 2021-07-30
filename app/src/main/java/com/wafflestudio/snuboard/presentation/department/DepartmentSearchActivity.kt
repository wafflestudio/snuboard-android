package com.wafflestudio.snuboard.presentation.department

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
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityDepartmentSearchBinding
import com.wafflestudio.snuboard.domain.model.TagDepartmentFull
import com.wafflestudio.snuboard.presentation.notice.HeartClickListener
import com.wafflestudio.snuboard.presentation.notice.NoticeInfiniteScrollListener
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class DepartmentSearchActivity : AppCompatActivity() {

    private val binding: ActivityDepartmentSearchBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_department_search
        ) as ActivityDepartmentSearchBinding
    }

    private val departmentSearchActivityViewModel: DepartmentSearchActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleEvent.triggerToast.observe(this@DepartmentSearchActivity) {
            it.getContentIfNotHandled()?.let { it1 ->
                Toast.makeText(this@DepartmentSearchActivity, it1, Toast.LENGTH_SHORT).show()
            }
        }
        val departmentInfo = intent.getSerializableExtra(DEPARTMENT_INFO) as TagDepartmentFull
        departmentSearchActivityViewModel.initiateDepartment(departmentInfo)
        binding.run {
            lifecycleOwner = this@DepartmentSearchActivity
            viewModel = departmentSearchActivityViewModel

            filterRecyclerView.run {
                val myLayoutManager = LinearLayoutManager(this@DepartmentSearchActivity)
                layoutManager = myLayoutManager
                val filterNoticeListAdapter = FilterSearchNoticeListAdapter(
                    HeartClickListener {
                        departmentSearchActivityViewModel.toggleSavedNotice(it)
                    },
                    departmentSearchActivityViewModel
                )
                filterNoticeListAdapter.setHasStableIds(true)
                adapter = filterNoticeListAdapter
                (adapter as FilterSearchNoticeListAdapter).registerAdapterDataObserver(
                    object : RecyclerView.AdapterDataObserver() {
                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                            if (positionStart == 0)
                                smoothScrollToPosition(0)
                        }
                    }
                )
                clearOnScrollListeners()
                addOnScrollListener(NoticeInfiniteScrollListener(myLayoutManager) {
                    departmentSearchActivityViewModel.getNotices()
                })
            }

            refreshLayout.setOnRefreshListener {
                departmentSearchActivityViewModel.updateNotices(
                    callback = {
                        refreshLayout.isRefreshing = false
                    }
                )
            }

            setSupportActionBar(toolBar)
            supportActionBar!!.apply {
                setDisplayShowTitleEnabled(false)
            }

            searchText.apply {
                postDelayed({
                    showSoftKeyboard(searchText)
                }, 200)
                setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        v: TextView?,
                        actionId: Int,
                        event: KeyEvent?
                    ): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            hideSoftKeyboard(searchText)
                            departmentSearchActivityViewModel.apply {
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
                departmentSearchActivityViewModel.cleanText()
                searchText.requestFocus()
                showSoftKeyboard(searchText)
            }

            departmentSearchActivityViewModel.notifyFilterNoticeList.observe(this@DepartmentSearchActivity) {
                filterRecyclerView.adapter?.notifyItemChanged(0, Unit)
            }

        }
        departmentSearchActivityViewModel.apply {
            updateNotice.observe(this@DepartmentSearchActivity) {
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
        val imm = this@DepartmentSearchActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        fun intentWithDepartmentInfo(context: Context, departmentInfo: TagDepartmentFull): Intent =
                Intent(context, DepartmentSearchActivity::class.java)
                        .putExtra(DEPARTMENT_INFO, departmentInfo as Serializable)

        const val DEPARTMENT_INFO = "DEPARTMENT_INFO"
    }
}
