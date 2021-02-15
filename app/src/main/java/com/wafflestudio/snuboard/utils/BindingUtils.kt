package com.wafflestudio.snuboard.utils

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout

@SuppressLint("RtlHardcoded")
@BindingAdapter("drawer_open")
fun bindAlertItem(view: DrawerLayout, bool: Boolean) {
    if (bool)
        view.openDrawer(Gravity.LEFT)
    else
        view.close()
}
