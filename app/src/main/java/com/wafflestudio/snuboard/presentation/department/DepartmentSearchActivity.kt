package com.wafflestudio.snuboard.presentation.department

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityDepartmentSearchBinding

class DepartmentSearchActivity : AppCompatActivity() {

    private val binding: ActivityDepartmentSearchBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_department_search
        ) as ActivityDepartmentSearchBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {

        }
    }
}
