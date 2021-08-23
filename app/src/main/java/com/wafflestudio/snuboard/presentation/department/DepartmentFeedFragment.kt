package com.wafflestudio.snuboard.presentation.department

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentDepartmentFeedBinding
import com.wafflestudio.snuboard.domain.model.DepartmentColor
import com.wafflestudio.snuboard.presentation.TagClickListener
import com.wafflestudio.snuboard.presentation.TagListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentFeedFragment : Fragment() {

    lateinit var binding: FragmentDepartmentFeedBinding
    private val departmentActivityViewModel: DepartmentActivityViewModel by activityViewModels()

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
        binding.run {
            lifecycleOwner = this@DepartmentFeedFragment
            activityViewModel = departmentActivityViewModel
            colorClickListener = ColorClickListener { departmentColor ->
                departmentActivityViewModel.changeDepartmentColor(departmentColor)
            }
            tagRecyclerView.run {
                adapter = TagListAdapter(
                        TagClickListener {
                            departmentActivityViewModel.toggleFollowingTag(it)
                        }
                )
                layoutManager = FlexboxLayoutManager(binding.root.context).apply {
                    flexWrap = FlexWrap.WRAP
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                }
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.app_bar_fragment_department_feed, menu)
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
            else -> false
        }
    }
}

class ColorClickListener(
        val clickListener: (departmentColor: DepartmentColor) -> Unit
) {
    fun onClick(departmentColor: DepartmentColor) =
            clickListener(departmentColor)
}
