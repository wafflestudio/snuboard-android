package com.wafflestudio.snuboard.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wafflestudio.snuboard.presentation.MainPageConst.DEPT
import com.wafflestudio.snuboard.presentation.MainPageConst.NOTICE
import com.wafflestudio.snuboard.presentation.MainPageConst.SAVED
import com.wafflestudio.snuboard.presentation.department.DepartmentFollowListFragment
import com.wafflestudio.snuboard.presentation.notice.NoticeFeedFragment
import com.wafflestudio.snuboard.presentation.user.SavedFragment

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        NOTICE -> NoticeFeedFragment()
        DEPT -> DepartmentFollowListFragment()
        SAVED -> SavedFragment()
        else -> {
            throw Error("Not valid fragment for designated page number")
        }
    }
}

object MainPageConst {
    const val NOTICE = 0
    const val DEPT = 1
    const val SAVED = 2
}