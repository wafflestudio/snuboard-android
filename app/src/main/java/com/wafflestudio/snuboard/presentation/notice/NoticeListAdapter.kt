package com.wafflestudio.snuboard.presentation.notice

import android.app.Activity
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ItemNoticeBinding
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.presentation.TagListAdapter


class NoticeListAdapter(private val heartClickListener: HeartClickListener?)
    : ListAdapter<Notice, NoticeViewHolder>(NoticeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bindItems(getItem(position), heartClickListener)
    }

}

class NoticeDiffCallback : DiffUtil.ItemCallback<Notice>() {
    override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
        return oldItem == newItem
    }

}

class NoticeViewHolder(private val binding: ItemNoticeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindItems(notice: Notice, heartOnClick: HeartClickListener?) {
        val tagRecyclerView = binding.root.findViewById<RecyclerView>(R.id.tag_recycler_view)
        tagRecyclerView.run {
            adapter = TagListAdapter(null)
            layoutManager = FlexboxLayoutManager(binding.root.context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
        }
        binding.run {
            item = notice
            clickListener = NoticeClickListener {
                binding.root.context.apply {
                    startActivity(
                            NoticeDetailActivity.intent(
                                    binding.root.context,
                                    it
                            )
                    )
                    ((this as ContextWrapper)
                            .baseContext as Activity)
                            .overridePendingTransition(
                                    R.anim.slide_from_right,
                                    R.anim.nav_default_exit_anim
                            )
                }
            }
            heartOnClick?.let {
                heartClickListener = it
            }
            executePendingBindings()
        }
    }
}

class NoticeClickListener(val clickListener: (itemId: Int) -> Unit) {
    fun onClick(itemId: Int) = clickListener(itemId)
}

class NoticeInfiniteScrollListener(private val layoutManager: LinearLayoutManager, val func: () -> Unit) :
        RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= firstVisibleItem + 2) {
                func()
                loading = true
            }
        }
    }
}

class HeartClickListener(val clickListener: (itemId: Int) -> Unit) {
    fun onClick(itemId: Int) = clickListener(itemId)
}
