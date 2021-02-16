package com.wafflestudio.snuboard.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentSavedBinding
import com.wafflestudio.snuboard.presentation.MainActivityViewModel

class SavedFragment : Fragment() {

    lateinit var binding: FragmentSavedBinding
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

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
            toolBar.also { tb ->
                tb.setNavigationIcon(R.drawable.ic_menu)
                tb.setNavigationOnClickListener {
                    mainActivityViewModel.setDrawer(true)
                }
            }
        }
        return binding.root
    }
}