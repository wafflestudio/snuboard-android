package com.wafflestudio.snuboard.presentation.department

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityDepartmentBinding
import com.wafflestudio.snuboard.domain.usecase.ClassifyDepartmentUseCase
import com.wafflestudio.snuboard.domain.usecase.GetNoticesByFollowUseCase
import com.wafflestudio.snuboard.domain.usecase.GetNoticesOfScrapUseCase
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DepartmentActivity : AppCompatActivity() {

    @Inject
    lateinit var classifyDepartmentUseCase: ClassifyDepartmentUseCase

    @Inject
    lateinit var getNoticesByFollowUseCase: GetNoticesByFollowUseCase

    @Inject
    lateinit var getNoticesOfScrapUseCase: GetNoticesOfScrapUseCase

    private val binding: ActivityDepartmentBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_department
        ) as ActivityDepartmentBinding
    }

    private val departmentActivityViewModel: DepartmentActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleEvent.triggerToast.observe(this) {
            it.getContentIfNotHandled()?.let { it1 ->
                val shortToast = Toast.makeText(this, it1, Toast.LENGTH_SHORT)
                shortToast.show()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ shortToast.cancel() }, 700)
            }
        }
        binding.run {
            lifecycleOwner = this@DepartmentActivity
            viewModel = departmentActivityViewModel

            pager.apply {
                adapter = DepartmentPagerAdapter(this@DepartmentActivity)
            }
            TabLayoutMediator(tabLayout, pager) { tab, position ->
                tab.text = DepartmentPageConst.fromPosition(position)!!.title
            }.attach()
            setSupportActionBar(toolBar)
            supportActionBar!!.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_navigate_before)
            }
        }

        val departmentId = intent.getIntExtra(EXTRA_DEPARTMENT_ID, -1)
        if (departmentId == -1) finish()
        departmentActivityViewModel.apply {
            updateNotice.observe(this@DepartmentActivity) {
                observeUpdateNotice(it)
            }
            getTagDepartmentInfo(departmentId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifyDepartmentUseCase.updateDepartments()
        getNoticesByFollowUseCase.updateNotices()
        getNoticesOfScrapUseCase.updateNotices()
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
        fun intent(context: Context, departmentId: Int) =
                Intent(context, DepartmentActivity::class.java)
                        .putExtra(EXTRA_DEPARTMENT_ID, departmentId)

        private const val EXTRA_DEPARTMENT_ID = "extra_department_id"
    }
}
