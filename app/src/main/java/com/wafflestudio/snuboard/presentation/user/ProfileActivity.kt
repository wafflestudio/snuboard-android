package com.wafflestudio.snuboard.presentation.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_profile
        ) as ActivityProfileBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {

        }
    }

    companion object {
        fun intent(context: Context) = Intent(context, ProfileActivity::class.java)
    }
}