package com.wafflestudio.snuboard.presentation.notice

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.databinding.ItemNoticeFileBinding
import com.wafflestudio.snuboard.domain.model.NoticeFile

class FileListAdapter() : ListAdapter<NoticeFile, FileViewHolder>(FileDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding =
            ItemNoticeFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bindItems(getItem(position))
    }

}

class FileDiffCallback : DiffUtil.ItemCallback<NoticeFile>() {
    override fun areItemsTheSame(oldItem: NoticeFile, newItem: NoticeFile): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoticeFile, newItem: NoticeFile): Boolean {
        return oldItem == newItem
    }

}

class FileViewHolder(private val binding: ItemNoticeFileBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindItems(file: NoticeFile) {
        binding.run {
            item = file
            clickListener = NoticeFileClickListener {
                binding.root.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
        }
    }
}

class NoticeFileClickListener(val clickListener: (link: String) -> Unit) {
    fun onClick(link: String) = clickListener(link)
}
