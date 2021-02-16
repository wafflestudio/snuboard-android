package com.wafflestudio.snuboard.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.Gravity
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.domain.model.Notice
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
    items?.run {
        adapt.submitList(items)
    }
}
