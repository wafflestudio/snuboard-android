package com.wafflestudio.snuboard.presentation.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentDepartmentFeedBinding

class DepartmentFeedFragment : Fragment() {

    lateinit var binding: FragmentDepartmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_department_feed,
                container,
                false
            )
        return binding.root
    }
}