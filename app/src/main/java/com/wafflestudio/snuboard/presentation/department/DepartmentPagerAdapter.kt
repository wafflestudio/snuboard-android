package com.wafflestudio.snuboard.presentation.department

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wafflestudio.snuboard.presentation.department.DepartmentPageConst.FEED
import com.wafflestudio.snuboard.presentation.department.DepartmentPageConst.HOME

class DepartmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        FEED.position -> DepartmentFeedFragment()
        HOME.position -> DepartmentHomeFragment()
        else -> {
            throw Error("Not valid fragment for designated page number")
        }
    }

}

enum class DepartmentPageConst(val position: Int, val title: String) {
    FEED(0, "관심태그"),
    HOME(1, "공지검색");

    companion object {
        fun fromPosition(position: Int): DepartmentPageConst? =
                values().find { it.position == position }
    }
}
