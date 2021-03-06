package com.wafflestudio.snuboard.presentation.auth

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    private val authActivityViewModel: AuthActivityViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_login,
                        container,
                        false
                )
        binding.run {
            activityViewModel = authActivityViewModel
            lifecycleOwner = this@LoginFragment
            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
            loginButton.setOnClickListener {
                authActivityViewModel.login()
            }
            passwordEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE
                            || (event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER))) {
                        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)
                                as InputMethodManager
                        imm.hideSoftInputFromWindow(v!!.windowToken, 0)
                        loginButton.performClick()
                        return true
                    }
                    return false
                }
            }
            )
        }
        return binding.root
    }

}
