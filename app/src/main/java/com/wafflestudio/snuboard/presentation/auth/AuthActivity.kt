package com.wafflestudio.snuboard.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    companion object {
        fun intent(context: Context): Intent = Intent(context, AuthActivity::class.java)
    }
}