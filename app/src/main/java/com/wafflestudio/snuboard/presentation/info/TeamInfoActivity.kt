package com.wafflestudio.snuboard.presentation.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.databinding.ActivityTeamInfoBinding
import com.wafflestudio.snuboard.domain.model.Team
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeamInfoActivity : AppCompatActivity() {

    private val binding: ActivityTeamInfoBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_team_info
        ) as ActivityTeamInfoBinding
    }

    @Inject
    lateinit var _team: Team

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            lifecycleOwner = this@TeamInfoActivity
            setSupportActionBar(toolBar)
            supportActionBar!!.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_navigate_before)
            }
            team = _team
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, TeamInfoActivity::class.java)
    }
}
