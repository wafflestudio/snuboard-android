package com.wafflestudio.snuboard.presentation.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentDepartmentFollowListBinding
import com.wafflestudio.snuboard.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentFollowListFragment : Fragment() {

    lateinit var binding: FragmentDepartmentFollowListBinding
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val departmentFollowListFragmentViewModel: DepartmentFollowListFragmentViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_department_follow_list,
                        container,
                        false
                )
        binding.run {
            lifecycleOwner = this@DepartmentFollowListFragment
            viewModel = departmentFollowListFragmentViewModel
            toolBar.also { tb ->
                tb.setNavigationIcon(R.drawable.ic_menu)
                tb.setNavigationOnClickListener {
                    mainActivityViewModel.setDrawer(true)
                }
            }
            followingDepartmentRecyclerView.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = FollowingDepartmentListAdapter()
            }
            departmentListRecyclerView.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = CollegeDepartmentListAdapter()
            }
        }
        departmentFollowListFragmentViewModel.getDepartments()
        return binding.root
    }
}