package com.wafflestudio.snuboard.presentation.department

import android.app.Activity
import android.content.ContextWrapper
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
import com.wafflestudio.snuboard.databinding.ItemFollowingDepartmentBinding
import com.wafflestudio.snuboard.domain.model.FollowingDepartment
import com.wafflestudio.snuboard.presentation.TagListAdapter

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
            item = followingDepartment
            clickListener = FollowingDepartmentClickListener {
                binding.root.apply {
                    context.startActivity(
                            DepartmentActivity.intent(
                                    context,
                                    followingDepartment.id
                            )
                    )
                    ((context as ContextWrapper)
                            .baseContext as Activity)
                            .overridePendingTransition(
                                    R.anim.slide_from_right,
                                    R.anim.nav_default_exit_anim
                            )
                }
            }
            executePendingBindings()
        }
    }
}

class FollowingDepartmentClickListener(val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}