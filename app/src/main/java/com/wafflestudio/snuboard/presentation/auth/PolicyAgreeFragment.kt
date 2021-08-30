package com.wafflestudio.snuboard.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentPolicyAgreeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyAgreeFragment : Fragment() {

    lateinit var binding: FragmentPolicyAgreeBinding

    private val authActivityViewModel: AuthActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_policy_agree,
            container,
            false
        )
        binding.run {
            activityViewModel = authActivityViewModel
            lifecycleOwner = this@PolicyAgreeFragment

            agreeButton.setOnClickListener {
                authActivityViewModel.signUp()
            }
        }

        return binding.root
    }
}
