package com.wafflestudio.snuboard.presentation.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityNotificationListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationListActivity : AppCompatActivity() {

    private val binding: ActivityNotificationListBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_notification_list
        ) as ActivityNotificationListBinding
    }

    private val notificationListActivityViewModel: NotificationListActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationListActivityViewModel.isNotificationActive.observe(this) {
            invalidateOptionsMenu()
        }
        notificationListActivityViewModel.getNotification()
        binding.run {
            lifecycleOwner = this@NotificationListActivity
            viewModel = notificationListActivityViewModel

            setSupportActionBar(toolBar)
            supportActionBar!!.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_navigate_before)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_activity_notification_list, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (notificationListActivityViewModel.isNotificationActive.value == true) {
            val icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_notifications_active, null)
            menu?.findItem(R.id.noti_button)?.icon = icon
        } else {
            val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_notifications_off, null)
            menu?.findItem(R.id.noti_button)?.icon = icon
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.noti_button ->
                notificationListActivityViewModel.toggleNotification()
            else ->
                return false
        }
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.slide_to_right)
    }

    companion object {
        fun intent(context: Context) =
            Intent(context, NotificationListActivity::class.java)
    }
}
