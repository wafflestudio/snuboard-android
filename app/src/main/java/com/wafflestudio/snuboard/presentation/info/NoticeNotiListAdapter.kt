package com.wafflestudio.snuboard.presentation.info

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
import com.wafflestudio.snuboard.data.room.NoticeNoti
import com.wafflestudio.snuboard.databinding.ItemNoticeNotiBinding
import com.wafflestudio.snuboard.presentation.TagListAdapter
import com.wafflestudio.snuboard.presentation.notice.NoticeDetailActivity


class NoticeNotiListAdapter() :
    ListAdapter<NoticeNoti, NoticeNotiViewHolder>(NoticeNotiDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeNotiViewHolder {
        val binding =
            ItemNoticeNotiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeNotiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeNotiViewHolder, position: Int) {
        holder.bindItems(getItem(position))
    }

}


class NoticeNotiDiffCallback : DiffUtil.ItemCallback<NoticeNoti>() {
    override fun areItemsTheSame(oldItem: NoticeNoti, newItem: NoticeNoti): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoticeNoti, newItem: NoticeNoti): Boolean {
        return oldItem == newItem
    }

}

class NoticeNotiViewHolder(val binding: ItemNoticeNotiBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bindItems(noticeNoti: NoticeNoti) {
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
            item = noticeNoti
            clickListener = NoticeNotiClickListener {
                binding.root.context.apply {
                    startActivity(
                        NoticeDetailActivity.intent(
                            binding.root.context,
                            it
                        )
                    )
                }
            }
        }
    }
}

class NoticeNotiClickListener(val clickListener: (itemId: Int) -> Unit) {
    fun onClick(itemId: Int) = clickListener(itemId)
}
