package com.wafflestudio.snuboard.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityMainBinding
import com.wafflestudio.snuboard.databinding.DialogSignoutBinding
import com.wafflestudio.snuboard.di.SharedPreferenceConst.ACCESS_TOKEN_KEY
import com.wafflestudio.snuboard.di.SharedPreferenceConst.REFRESH_TOKEN_KEY
import com.wafflestudio.snuboard.domain.usecase.FCMTopicUseCase
import com.wafflestudio.snuboard.presentation.MainPageConst.DEPT
import com.wafflestudio.snuboard.presentation.MainPageConst.NOTICE
import com.wafflestudio.snuboard.presentation.MainPageConst.SAVED
import com.wafflestudio.snuboard.presentation.auth.AuthActivity
import com.wafflestudio.snuboard.presentation.auth.PolicyReadActivity
import com.wafflestudio.snuboard.presentation.info.NotificationListActivity
import com.wafflestudio.snuboard.presentation.info.TeamInfoActivity
import com.wafflestudio.snuboard.presentation.info.VersionInfoActivity
import com.wafflestudio.snuboard.utils.EmailUtils
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        ) as ActivityMainBinding
    }

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleEvent.triggerToast.observe(this) {
            it.getContentIfNotHandled()?.let { it1 ->
                Toast.makeText(this, it1, Toast.LENGTH_SHORT).show()
            }
        }
        binding.run {
            lifecycleOwner = this@MainActivity
            viewModel = mainActivityViewModel

            pager.apply {
                adapter = MainPagerAdapter(this@MainActivity)
                isUserInputEnabled = false
            }
            bottomNavigation.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.notice_feed_fragment -> pager.setCurrentItem(NOTICE, true)
                    R.id.department_follow_list_fragment -> pager.setCurrentItem(DEPT, true)
                    R.id.saved_fragment -> pager.setCurrentItem(SAVED, true)
                    else -> {
                        throw Error("Invalid itemId in bottomNavigation")
                    }
                }
                true
            }
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.group1_item1 -> {
                        startActivity(VersionInfoActivity.intent(this@MainActivity))
                    }
                    R.id.group1_item2 -> {
                        startActivity(PolicyReadActivity.intent(this@MainActivity))
                    }
                    R.id.group1_item3 -> {
                        EmailUtils.sendEmailToAdmin(
                            this@MainActivity,
                            "개발자에게 메일 보내기",
                            Array<String?>(1) { "snuboard@wafflestudio.com" })
                    }
                    R.id.group1_item4 -> {
                        startActivity(TeamInfoActivity.intent(this@MainActivity))
                    }
                    R.id.group2_item1 -> {
                        startActivity(NotificationListActivity.intent(this@MainActivity))
                        overridePendingTransition(
                            R.anim.slide_from_right,
                            R.anim.nav_default_exit_anim
                        )
                    }
                    R.id.group3_item1 -> {
                        val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                        pref.edit {
                            remove(REFRESH_TOKEN_KEY)
                            remove(ACCESS_TOKEN_KEY)
                        }
                        mainActivityViewModel.unSubscribe().continueWith {
                            if (it.isSuccessful)
                                startActivity(AuthActivity.intent(this@MainActivity))
                            else {
                                SingleEvent.triggerToast.value = Event(
                                    "심각한 오류가 발생했습니다. 앱을 재설치해주세요."
                                )
                            }
                            finish()
                        }
                    }
                    R.id.group3_item2 -> {
                        CustomAlertDialog(mainActivityViewModel)
                            .show(supportFragmentManager, "AlertDialog")
                    }
                    else ->
                        return@setNavigationItemSelectedListener false
                }
                drawerLayout.close()
                return@setNavigationItemSelectedListener true
            }
        }
        mainActivityViewModel.navigateToAuthActivity.observe(this) {
            it.getContentIfNotHandled()?.let {
                val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                pref.edit {
                    remove(REFRESH_TOKEN_KEY)
                    remove(ACCESS_TOKEN_KEY)
                }
                startActivity(AuthActivity.intent(this@MainActivity))
                finish()
            }
        }
        mainActivityViewModel.getMyInfo()
    }

    override fun onBackPressed() {
        binding.run {
            var pageId: Int? = null
            when (pager.currentItem) {
                NOTICE -> {
                    finish()
                }
                DEPT -> {
                    pageId = R.id.notice_feed_fragment
                }
                SAVED -> {
                    pageId = R.id.notice_feed_fragment
                }
            }
            pageId?.let {
                bottomNavigation.selectedItemId = it
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}

class CustomAlertDialog(
        private val mainActivityViewModel: MainActivityViewModel
) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DialogSignoutBinding.inflate(LayoutInflater.from(context))
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding.run {
            val email = mainActivityViewModel.myInfo.value!!.email
            val content = String.format(resources.getString(R.string.dialog_signout_content), """<b>$email</b>""")
            alertContent.text = Html.fromHtml(content)
            confirmEmail.hint = email
            dismissButton.setOnClickListener {
                this@CustomAlertDialog.dismiss()
            }
            confirmEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() == email) {
                        signOutButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        signOutButton.isClickable = true
                    } else {
                        signOutButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.transparent_red))
                        signOutButton.isClickable = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
            confirmEmail.setOnEditorActionListener(object : TextView.OnEditorActionListener {
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
            })
            signOutButton.setOnClickListener {
                mainActivityViewModel.signOut()
            }
        }
        return binding.root
    }
}
