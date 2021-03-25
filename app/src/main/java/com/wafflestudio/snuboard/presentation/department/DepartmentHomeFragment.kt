package com.wafflestudio.snuboard.presentation.department

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentDepartmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentHomeFragment : Fragment() {

    lateinit var binding: FragmentDepartmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_department_home,
                        container,
                        false
                )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.app_bar_fragment_department_home, menu)
    }
}
