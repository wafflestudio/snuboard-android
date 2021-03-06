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
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding

    private val authActivityViewModel: AuthActivityViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_sign_up,
                        container,
                        false
                )
        binding.run {
            activityViewModel = authActivityViewModel
            lifecycleOwner = this@SignUpFragment
            signUpButton.setOnClickListener {
                authActivityViewModel.signUp()
            }
            nicknameEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE
                            || (event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER))) {
                        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)
                                as InputMethodManager
                        imm.hideSoftInputFromWindow(v!!.windowToken, 0)
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