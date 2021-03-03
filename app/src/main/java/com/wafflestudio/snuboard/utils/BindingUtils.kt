package com.wafflestudio.snuboard.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.domain.model.*
import com.wafflestudio.snuboard.presentation.TagListAdapter
import com.wafflestudio.snuboard.presentation.department.CollegeDepartmentListAdapter
import com.wafflestudio.snuboard.presentation.department.FollowingDepartmentListAdapter
import com.wafflestudio.snuboard.presentation.notice.NoticeListAdapter

@SuppressLint("RtlHardcoded")
@BindingAdapter("drawer_open")
fun bindDrawerOpen(view: DrawerLayout, bool: Boolean) {
    if (bool)
        view.openDrawer(Gravity.LEFT)
    else
        view.close()
}

@BindingAdapter("heart_filled")
fun bindHeartFilled(view: ImageView, bool: Boolean) {
    if (bool) {
        view.setImageResource(R.drawable.ic_favorite)
        view.imageTintList = ColorStateList
                .valueOf(ContextCompat.getColor(view.context, R.color.purple1))
    } else {
        view.setImageResource(R.drawable.ic_favorite_border)
        view.imageTintList = ColorStateList
                .valueOf(ContextCompat.getColor(view.context, R.color.gray3))
    }
}

@BindingAdapter("notice_items")
fun bindNoticeItems(view: RecyclerView, items: List<Notice>?) {
    val adapt = view.adapter as NoticeListAdapter
    items?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("notice_tag_items")
fun bindNoticeTagItems(view: RecyclerView, item: Notice?) {
    val adapt = view.adapter as TagListAdapter
    item?.also {
        val items = mutableListOf<Tag>(
                Tag(it.departmentName, it.departmentColor)
        )
        items.addAll(
                it.tags.map { it1 ->
                    Tag(it1, DepartmentColor.TAG_COLOR)
                }
        )
        adapt.submitList(items)
    }
}

@BindingAdapter("string_tag_items")
fun bindStringTagItems(view: RecyclerView, contents: List<String>?) {
    val adapt = view.adapter as TagListAdapter
    contents?.also {
        adapt.submitList(
                it.map { it1 ->
                    Tag(it1, DepartmentColor.TAG_COLOR)
                }
        )
    }
}

@BindingAdapter("tag_items")
fun bindTagItems(view: RecyclerView, tags: List<Tag>?) {
    val adapt = view.adapter as TagListAdapter
    tags?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("college_department_items")
fun bindCollegeDepartmentItems(view: RecyclerView, items: List<CollegeDepartment>?) {
    val adapt = view.adapter as CollegeDepartmentListAdapter
    items?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("following_department_items")
fun bindFollowingDepartmentItems(view: RecyclerView, items: List<FollowingDepartment>?) {
    val adapt = view.adapter as FollowingDepartmentListAdapter
    items?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("tag_color")
fun bindTagColor(view: CardView, colorId: Int) {
    val colorString = String.format("#%08x", ContextCompat.getColor(view.context, colorId))
    view.setCardBackgroundColor(Color.parseColor(colorString))
}

@BindingAdapter("department_color")
fun bindDepartmentColor(view: ConstraintLayout, colorId: Int) {
    val colorString = String.format("#%08x", ContextCompat.getColor(view.context, colorId))
    view.setBackgroundColor(Color.parseColor(colorString))
}

@BindingAdapter("preview_color")
fun bindPreviewColor(view: ImageView, colorId: Int) {
    val colorString = String.format("#%08x", ContextCompat.getColor(view.context, colorId))
    view.setColorFilter(Color.parseColor(colorString))
}

@BindingAdapter("text_style_string")
fun bindTextStyleString(view: TextView, style: String) {
    when (style) {
        "bold" -> view.setTypeface(null, Typeface.BOLD)
        "normal" -> view.setTypeface(null, Typeface.NORMAL)
        else -> throw Error("Not valid style in textView")
    }
}

@BindingAdapter("image_visibility_string")
fun bindTextStyleString(view: ImageView, visibility: String) {
    when (visibility) {
        "visible" -> view.visibility = View.VISIBLE
        "gone" -> view.visibility = View.GONE
        else -> throw Error("Not valid style in textView")
    }
}

@BindingAdapter("click_listener_color")
fun bindClickListenerColor(view: ConstraintLayout, clickListener: () -> Unit) {
    view.setOnClickListener {
        clickListener()
    }
}