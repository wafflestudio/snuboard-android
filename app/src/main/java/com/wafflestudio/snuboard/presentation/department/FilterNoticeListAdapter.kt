package com.wafflestudio.snuboard.presentation.department

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wafflestudio.snuboard.databinding.ItemNoticeBinding
import com.wafflestudio.snuboard.databinding.ItemNoticeFilterBinding
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.presentation.TagClickListener
import com.wafflestudio.snuboard.presentation.TagListAdapter
import com.wafflestudio.snuboard.presentation.notice.HeartClickListener
import com.wafflestudio.snuboard.presentation.notice.NoticeViewHolder
import com.wafflestudio.snuboard.utils.bindVisibilityString


class FilterNoticeListAdapter(
        private val heartClickListener: HeartClickListener?,
        private val departmentActivityViewModel: DepartmentActivityViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = listOf<Notice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FILTER_VIEW_TYPE -> {
                val binding = ItemNoticeFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FilterViewHolder(binding)
            }
            NOTICE_VIEW_TYPE -> {
                val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NoticeViewHolder(binding)
            }
            else -> {
                throw Error("Not right View type in FilterNoticeListAdapter")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilterViewHolder ->
                holder.bindItems(departmentActivityViewModel)
            is NoticeViewHolder ->
                holder.bindItems(items[position - 1], heartClickListener)
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItemId(position: Int): Long {
        return if (position == 0)
            -1
        else
            items[position - 1].id.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            FILTER_VIEW_TYPE
        else
            NOTICE_VIEW_TYPE
    }

    companion object {
        private const val FILTER_VIEW_TYPE = 0
        private const val NOTICE_VIEW_TYPE = 1
    }
}

class FilterViewHolder(private val binding: ItemNoticeFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bindItems(departmentActivityViewModel: DepartmentActivityViewModel) {
        binding.run {
            activityViewModel = departmentActivityViewModel
            tagRecyclerView.run {
                adapter = TagListAdapter(
                        TagClickListener {
                            departmentActivityViewModel.toggleHomeTag(it)
                        }
                )
                layoutManager = FlexboxLayoutManager(binding.root.context).apply {
                    flexWrap = FlexWrap.WRAP
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                }
            }
            applyButton.setOnClickListener {
                departmentActivityViewModel.applyHomeTags()
                hideSoftKeyboard(searchText)
                searchText.clearFocus()
                departmentActivityViewModel.adaptScrollListener()
            }
            eraseButton.setOnClickListener {
                departmentActivityViewModel.eraseHomeTags()
                hideSoftKeyboard(searchText)
                searchText.clearFocus()
                departmentActivityViewModel.adaptScrollListener()
            }
            eraseSearchButton.setOnClickListener {
                searchText.requestFocus()
                showSoftKeyboard(searchText)
                departmentActivityViewModel.run {
                    cleanText()
                }
                searchText.text = SpannableStringBuilder("")
                bindVisibilityString(it, "gone")
            }
            searchText.apply {
//                postDelayed({
//                    showSoftKeyboard(searchText)
//                }, 200)
                setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                            v: TextView?,
                            actionId: Int,
                            event: KeyEvent?
                    ): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            hideSoftKeyboard(searchText)
                            departmentActivityViewModel.apply {
                                cleanCursor()
                                clearNotices()
                                applyHomeTags()
                            }
                            searchText.clearFocus()
                            return true
                        }
                        return false
                    }
                })
                addTextChangedListener {
                    departmentActivityViewModel.notifyList()
                }
            }
            executePendingBindings()
        }
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = binding.root.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideSoftKeyboard(view: View) {
        val imm = binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
