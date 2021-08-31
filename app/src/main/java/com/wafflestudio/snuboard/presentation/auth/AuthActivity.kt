package com.wafflestudio.snuboard.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityAuthBinding
import com.wafflestudio.snuboard.presentation.MainActivity
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val binding: ActivityAuthBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_auth
        ) as ActivityAuthBinding
    }

    private val authActivityViewModel: AuthActivityViewModel by viewModels()

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(this)

        appUpdateManager.let {
            it.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        REQUEST_CODE_UPDATE
                    )
                }
            }
        }
        authActivityViewModel.apply {
            navigateToMainActivity.observe(this@AuthActivity) {
                it.getContentIfNotHandled()?.let {
                    startActivity(MainActivity.intent(this@AuthActivity))
                    finish()
                }
            }
            checkToken()
        }
        SingleEvent.triggerToast.observe(this) {
            it.getContentIfNotHandled()?.let { it1 ->
                Toast.makeText(this, it1, Toast.LENGTH_SHORT).show()
            }
        }
        binding.run {

        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(
                    this,
                    "업데이트에 실패했습니다. 정상적인 이용을 위하여 앱 업데이트를 진행해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE_UPDATE
                )
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, AuthActivity::class.java)

        const val REQUEST_CODE_UPDATE = 100
    }
}
