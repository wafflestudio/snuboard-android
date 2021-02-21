package com.wafflestudio.snuboard.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityMainBinding
import com.wafflestudio.snuboard.presentation.MainPageConst.DEPT
import com.wafflestudio.snuboard.presentation.MainPageConst.NOTICE
import com.wafflestudio.snuboard.presentation.MainPageConst.SAVED
import com.wafflestudio.snuboard.presentation.user.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        ) as ActivityMainBinding
    }

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            lifecycleOwner = this@MainActivity
            viewModel = mainActivityViewModel

            pager.apply {
                adapter = MainPagerAdapter(this@MainActivity)
                isUserInputEnabled = false
            }
            bottomNavigation.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.notice_feed_fragment -> pager.setCurrentItem(NOTICE, false)
                    R.id.department_follow_list_fragment -> pager.setCurrentItem(DEPT, false)
                    R.id.saved_fragment -> pager.setCurrentItem(SAVED, false)
                    else -> {
                        throw Error("Invalid itemId in bottomNavigation")
                    }
                }
                true
            }
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.group1_item1 -> {
                        startActivity(ProfileActivity.intent(this@MainActivity))
                        overridePendingTransition(
                            R.anim.slide_from_right,
                            R.anim.nav_default_exit_anim
                        )
                    }
                    else ->
                        return@setNavigationItemSelectedListener false
                }
                drawerLayout.close()
                return@setNavigationItemSelectedListener true
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}