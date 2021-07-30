package com.wafflestudio.snuboard.presentation.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityVersionInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class VersionInfoActivity : AppCompatActivity() {

    private val binding: ActivityVersionInfoBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_version_info
        ) as ActivityVersionInfoBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            lifecycleOwner = this@VersionInfoActivity
            setSupportActionBar(toolBar)
            supportActionBar!!.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_navigate_before)
            }
            val packageManager = packageManager
            val time = packageManager.getPackageInfo(packageName, 0).lastUpdateTime
            val timeString = SimpleDateFormat.getInstance().format(Date(time))
            val version = packageManager.getPackageInfo(packageName, 0).versionName
            info.text = getString(R.string.version_info_explanation, version, timeString)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, VersionInfoActivity::class.java)
    }
}
