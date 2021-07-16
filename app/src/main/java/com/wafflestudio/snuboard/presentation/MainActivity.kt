package com.wafflestudio.snuboard.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityMainBinding
import com.wafflestudio.snuboard.di.SharedPreferenceConst.ACCESS_TOKEN_KEY
import com.wafflestudio.snuboard.di.SharedPreferenceConst.REFRESH_TOKEN_KEY
import com.wafflestudio.snuboard.presentation.MainPageConst.DEPT
import com.wafflestudio.snuboard.presentation.MainPageConst.NOTICE
import com.wafflestudio.snuboard.presentation.MainPageConst.SAVED
import com.wafflestudio.snuboard.presentation.auth.AuthActivity
import com.wafflestudio.snuboard.presentation.auth.PolicyReadActivity
import com.wafflestudio.snuboard.utils.SingleEvent
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
        SingleEvent.triggerToast.observe(this) {
            it.getContentIfNotHandled()?.let { it1 ->
                Toast.makeText(this, it1, Toast.LENGTH_SHORT).show()
            }
        }
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
                    R.id.group1_item2 -> {
                        startActivity(PolicyReadActivity.intent(this@MainActivity))
                    }
                    R.id.group3_item1 -> {
                        val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                        pref.edit {
                            remove(REFRESH_TOKEN_KEY)
                            remove(ACCESS_TOKEN_KEY)
                        }
                        startActivity(AuthActivity.intent(this@MainActivity))
                        finish()
                    }
                    else ->
                        return@setNavigationItemSelectedListener false
                }
                drawerLayout.close()
                return@setNavigationItemSelectedListener true
            }
        }
        mainActivityViewModel.navigateToAuthActivity.observe(this) {
            it.getContentIfNotHandled()?.let {
                val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                pref.edit {
                    remove(REFRESH_TOKEN_KEY)
                    remove(ACCESS_TOKEN_KEY)
                }
                startActivity(AuthActivity.intent(this@MainActivity))
                finish()
            }
        }
        mainActivityViewModel.getMyInfo()
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
