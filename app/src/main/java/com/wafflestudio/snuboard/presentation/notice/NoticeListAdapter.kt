package com.wafflestudio.snuboard.presentation.notice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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


class NoticeListAdapter : ListAdapter<Notice, NoticeViewHolder>(NoticeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bindItems(getItem(position))
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
    fun bindItems(notice: Notice) {
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
                binding.root.context.startActivity(
                        NoticeDetailActivity.intent(
                                binding.root.context,
                                it
                        )
                )
            }
            executePendingBindings()
        }
    }
}

class NoticeClickListener(val clickListener: (itemId: Int) -> Unit) {
    fun onClick(itemId: Int) = clickListener(itemId)
}
