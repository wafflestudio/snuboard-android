package com.wafflestudio.snuboard.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityPolicyReadBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyReadActivity : AppCompatActivity() {

    private val binding: ActivityPolicyReadBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_policy_read
        ) as ActivityPolicyReadBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            readButton.setOnClickListener {
                finish()
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, PolicyReadActivity::class.java)
    }
}
