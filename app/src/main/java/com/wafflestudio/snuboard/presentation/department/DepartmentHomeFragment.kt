package com.wafflestudio.snuboard.presentation.department

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentDepartmentHomeBinding
import com.wafflestudio.snuboard.presentation.notice.HeartClickListener
import com.wafflestudio.snuboard.presentation.notice.NoticeInfiniteScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentHomeFragment : Fragment() {

    lateinit var binding: FragmentDepartmentHomeBinding
    private val departmentActivityViewModel: DepartmentActivityViewModel by activityViewModels()

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
        binding.run {
            lifecycleOwner = this@DepartmentHomeFragment
            activityViewModel = departmentActivityViewModel
            filterNoticeRecyclerView.run {
                val myLayoutManager = LinearLayoutManager(requireContext())
                layoutManager = myLayoutManager
                val filterNoticeListAdapter = FilterNoticeListAdapter(
                        HeartClickListener {
                            departmentActivityViewModel.toggleSavedNotice(it)
                        },
                        departmentActivityViewModel
                )
                filterNoticeListAdapter.setHasStableIds(true)
                adapter = filterNoticeListAdapter
                (adapter as FilterNoticeListAdapter).registerAdapterDataObserver(
                        object : RecyclerView.AdapterDataObserver() {
                            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                                if (positionStart == 0)
                                    smoothScrollToPosition(0)
                            }
                        }
                )
                clearOnScrollListeners()
                addOnScrollListener(NoticeInfiniteScrollListener(myLayoutManager) {
                    departmentActivityViewModel.getNotices()
                })
            }
            departmentActivityViewModel.notifyFilterNoticeList.observe(viewLifecycleOwner) {
                filterNoticeRecyclerView.adapter?.notifyItemChanged(0, Unit)
            }
            refreshLayout.setOnRefreshListener {
                departmentActivityViewModel.updateNotices(callback = {
                    refreshLayout.isRefreshing = false
                })
                filterNoticeRecyclerView.run {
                    clearOnScrollListeners()
                    addOnScrollListener(NoticeInfiniteScrollListener(layoutManager as LinearLayoutManager) {
                        departmentActivityViewModel.getNotices()
                    })
                }
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.app_bar_fragment_department_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.link_button -> {
                val url = departmentActivityViewModel.tagDepartmentInfo.value?.link
                url?.let {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                }
                return true
            }
            R.id.search_button -> {
                departmentActivityViewModel.tagDepartmentInfo.value?.let {
                    startActivity(DepartmentSearchActivity
                            .intentWithDepartmentInfo(requireContext(), it))
                    activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.nav_default_exit_anim)
                }
                true
            }
            else -> false
        }
    }
}
