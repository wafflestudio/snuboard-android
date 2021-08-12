package com.wafflestudio.snuboard.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentSavedBinding
import com.wafflestudio.snuboard.presentation.MainActivityViewModel
import com.wafflestudio.snuboard.presentation.notice.HeartClickListener
import com.wafflestudio.snuboard.presentation.notice.NoticeInfiniteScrollListener
import com.wafflestudio.snuboard.presentation.notice.NoticeListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment() {

    lateinit var binding: FragmentSavedBinding
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val savedFragmentViewModel: SavedFragmentViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_saved,
                        container,
                        false
                )
        binding.run {
            lifecycleOwner = this@SavedFragment
            fragmentViewModel = savedFragmentViewModel
            toolBar.also { tb ->
                tb.setNavigationIcon(R.drawable.ic_menu)
                tb.setNavigationOnClickListener {
                    mainActivityViewModel.setDrawer(true)
                }
            }
            recyclerView.run {
                val myLayoutManager = LinearLayoutManager(requireContext())
                layoutManager = myLayoutManager
                adapter = NoticeListAdapter(
                        HeartClickListener {
                            savedFragmentViewModel.toggleSavedNotice(it)
                        }
                )
                (adapter as NoticeListAdapter).registerAdapterDataObserver(
                        object : RecyclerView.AdapterDataObserver() {
                            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                                if (positionStart == 0)
                                    smoothScrollToPosition(0)
                            }
                        }
                )
                clearOnScrollListeners()
                addOnScrollListener(NoticeInfiniteScrollListener(myLayoutManager) {
                    savedFragmentViewModel.getSavedNotices()
                })
            }
            refreshLayout.setOnRefreshListener {
                savedFragmentViewModel.updateSavedNotices {
                    refreshLayout.isRefreshing = false
                }
                recyclerView.run {
                    clearOnScrollListeners()
                    addOnScrollListener(NoticeInfiniteScrollListener(layoutManager as LinearLayoutManager) {
                        savedFragmentViewModel.getSavedNotices()
                    })
                }
            }
        }
        savedFragmentViewModel.apply {
            updateSavedNotices.observe(viewLifecycleOwner) {
                updateSavedNotices()
                binding.recyclerView.run {
                    val myLayoutManager = layoutManager as LinearLayoutManager
                    smoothScrollToPosition(0)
                    clearOnScrollListeners()
                    addOnScrollListener(NoticeInfiniteScrollListener(myLayoutManager) {
                        savedFragmentViewModel.getSavedNotices()
                    })
                }
            }
            updateSavedNotice.observe(viewLifecycleOwner) {
                observeUpdateNotice(it)
            }
            getSavedNotices()
        }
        return binding.root
    }
}
