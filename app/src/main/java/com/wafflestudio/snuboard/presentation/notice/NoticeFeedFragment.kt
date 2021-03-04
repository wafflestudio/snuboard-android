package com.wafflestudio.snuboard.presentation.notice

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
import com.wafflestudio.snuboard.databinding.FragmentNoticeFeedBinding
import com.wafflestudio.snuboard.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeFeedFragment : Fragment() {

    lateinit var binding: FragmentNoticeFeedBinding
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val noticeFeedFragmentViewModel: NoticeFeedFragmentViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_notice_feed,
                        container,
                        false
                )
        binding.run {
            lifecycleOwner = this@NoticeFeedFragment
            fragmentViewModel = noticeFeedFragmentViewModel
            toolBar.also { tb ->
                tb.inflateMenu(R.menu.app_bar_fragment_notice_feed)
                tb.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.search_button ->
                            startActivity(NoticeSearchActivity.intent(requireContext()))
                        else -> return@setOnMenuItemClickListener false
                    }
                    return@setOnMenuItemClickListener true
                }
                tb.setNavigationIcon(R.drawable.ic_menu)
                tb.setNavigationOnClickListener {
                    mainActivityViewModel.setDrawer(true)
                }
            }
            recyclerView.run {
                val myLayoutManager = LinearLayoutManager(requireContext())
                layoutManager = myLayoutManager
                adapter = NoticeListAdapter()
                clearOnScrollListeners()
                addOnScrollListener(NoticeInfiniteScrollListener(myLayoutManager) {
                    noticeFeedFragmentViewModel.getNotices()
                })
            }
        }
        noticeFeedFragmentViewModel.apply {
            updateNotices.observe(viewLifecycleOwner, {
                updateNotices()
            })
            getNotices()
        }
        return binding.root
    }
}