package com.wafflestudio.snuboard.presentation.department

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityDepartmentBinding
import com.wafflestudio.snuboard.domain.usecase.ClassifyDepartmentUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DepartmentActivity : AppCompatActivity() {

    @Inject
    lateinit var classifyDepartmentUseCase: ClassifyDepartmentUseCase

    private val binding: ActivityDepartmentBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_department
        ) as ActivityDepartmentBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifyDepartmentUseCase.updateDepartments()
    }

    companion object {
        fun intent(context: Context, departmentId: Int) =
                Intent(context, DepartmentActivity::class.java)
                        .putExtra(EXTRA_DEPARTMENT_ID, departmentId)

        private const val EXTRA_DEPARTMENT_ID = "extra_department_id"
    }
}