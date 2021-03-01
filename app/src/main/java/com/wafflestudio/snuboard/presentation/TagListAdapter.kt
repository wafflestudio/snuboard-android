package com.wafflestudio.snuboard.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.databinding.SubItemTagBinding
import com.wafflestudio.snuboard.domain.model.Tag

class TagListAdapter(private val tagClickListener: TagClickListener?)
    : ListAdapter<Tag, TagViewHolder>(TagDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = SubItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bindItems(getItem(position), tagClickListener)
    }
}


class TagDiffCallback : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }

}

class TagViewHolder(private val binding: SubItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bindItems(tag: Tag, tagClickListener: TagClickListener?) {
        binding.run {
            item = tag
            tagClickListener?.let {
                tagCard.setOnClickListener {
                    tagClickListener.onClick(tag.content)
                }
            }
            executePendingBindings()
        }
    }
}

class TagClickListener(val clickListener: (itemContent: String) -> Unit) {
    fun onClick(itemContent: String) = clickListener(itemContent)
}
