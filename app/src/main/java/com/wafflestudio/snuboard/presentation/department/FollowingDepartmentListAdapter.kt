package com.wafflestudio.snuboard.presentation.department

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.databinding.ItemFollowingDepartmentBinding
import com.wafflestudio.snuboard.domain.model.FollowingDepartment

class FollowingDepartmentListAdapter :
        ListAdapter<FollowingDepartment, FollowingDepartmentViewHolder>(FollowingDepartmentDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingDepartmentViewHolder {
        val binding = ItemFollowingDepartmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingDepartmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingDepartmentViewHolder, position: Int) {
        holder.bindItems(getItem(position))
    }
}

class FollowingDepartmentDiffCallback : DiffUtil.ItemCallback<FollowingDepartment>() {
    override fun areItemsTheSame(oldItem: FollowingDepartment, newItem: FollowingDepartment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FollowingDepartment, newItem: FollowingDepartment): Boolean {
        return oldItem == newItem
    }

}

class FollowingDepartmentViewHolder(private val binding: ItemFollowingDepartmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bindItems(followingDepartment: FollowingDepartment) {
        binding.run {
            item = followingDepartment
        }
    }
}