package com.wafflestudio.snuboard.presentation.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            recyclerView.run {
                val myLayoutManager = LinearLayoutManager(this@NotificationListActivity)
                layoutManager = myLayoutManager
                adapter = NoticeNotiListAdapter()

                val simpleItemCallback = object : ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        (viewHolder as NoticeNotiViewHolder)
                                .binding.item?.id?.let {
                                    notificationListActivityViewModel.deleteNoticeNoti(it)
                                            .subscribe()
                                }
                        Toast.makeText(applicationContext, "삭제되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }
                val itemTouchHelper = ItemTouchHelper(simpleItemCallback)
                itemTouchHelper.attachToRecyclerView(this)
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
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
