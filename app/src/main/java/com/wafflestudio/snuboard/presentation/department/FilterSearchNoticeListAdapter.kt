package com.wafflestudio.snuboard.presentation.department

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wafflestudio.snuboard.databinding.ItemNoticeBinding
import com.wafflestudio.snuboard.databinding.ItemNoticeSearchFilterBinding
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.presentation.TagClickListener
import com.wafflestudio.snuboard.presentation.TagListAdapter
import com.wafflestudio.snuboard.presentation.notice.HeartClickListener
import com.wafflestudio.snuboard.presentation.notice.NoticeViewHolder

class FilterSearchNoticeListAdapter(
    private val heartClickListener: HeartClickListener?,
    private val departmentSearchActivityViewModel: DepartmentSearchActivityViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = listOf<Notice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FILTER_VIEW_TYPE -> {
                val binding = ItemNoticeSearchFilterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FilterSearchViewHolder(binding)
            }
            NOTICE_VIEW_TYPE -> {
                val binding =
                    ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NoticeViewHolder(binding)
            }
            else -> {
                throw Error("Not right View type in FilterNoticeListAdapter")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilterSearchViewHolder ->
                holder.bindItems(departmentSearchActivityViewModel)
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

class FilterSearchViewHolder(private val binding: ItemNoticeSearchFilterBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindItems(departmentSearchActivityViewModel: DepartmentSearchActivityViewModel) {
        binding.run {
            activityViewModel = departmentSearchActivityViewModel
            tagRecyclerView.run {
                adapter = TagListAdapter(
                    TagClickListener {
                        departmentSearchActivityViewModel.toggleHomeTag(it)
                    }
                )
                layoutManager = FlexboxLayoutManager(binding.root.context).apply {
                    flexWrap = FlexWrap.WRAP
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                }
            }
            applyButton.setOnClickListener {
                departmentSearchActivityViewModel.applyHomeTags()
            }
            eraseButton.setOnClickListener {
                departmentSearchActivityViewModel.eraseHomeTags()
            }
            executePendingBindings()
        }
    }
}
