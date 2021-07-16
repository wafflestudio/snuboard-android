package com.wafflestudio.snuboard.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding

    private val authActivityViewModel: AuthActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sign_in,
                container,
                false
            )
        binding.run {
            activityViewModel = authActivityViewModel
            lifecycleOwner = this@SignInFragment
            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_policyAgreeFragment)
            }
            loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_login_fragment)
            }
        }
        return binding.root
    }

}
