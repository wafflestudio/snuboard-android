package com.wafflestudio.snuboard.presentation.department

import android.app.Activity
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ItemCollegeDepartmentBinding
import com.wafflestudio.snuboard.domain.model.CollegeDepartment

class CollegeDepartmentListAdapter :
        ListAdapter<CollegeDepartment, CollegeDepartmentViewHolder>(CollegeDepartmentDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollegeDepartmentViewHolder {
        val binding = ItemCollegeDepartmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollegeDepartmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollegeDepartmentViewHolder, position: Int) {
        holder.bindItems(getItem(position))
    }

}

class CollegeDepartmentDiffCallback : DiffUtil.ItemCallback<CollegeDepartment>() {
    override fun areItemsTheSame(oldItem: CollegeDepartment, newItem: CollegeDepartment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CollegeDepartment, newItem: CollegeDepartment): Boolean {
        return oldItem == newItem
    }

}

class CollegeDepartmentViewHolder(private val binding: ItemCollegeDepartmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bindItems(collegeDepartment: CollegeDepartment) {
        binding.run {
            item = collegeDepartment
            root.apply {
                setOnClickListener {
                    context.startActivity(
                            DepartmentActivity.intent(
                                    context,
                                    collegeDepartment.id
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
