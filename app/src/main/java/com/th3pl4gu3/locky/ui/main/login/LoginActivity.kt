package com.th3pl4gu3.locky.ui.main.login

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.databinding.ActivityLoginBinding
import com.th3pl4gu3.locky.ui.main.utils.activateDarkStatusBar
import com.th3pl4gu3.locky.ui.main.utils.activateLightAccentStatusBar
import com.th3pl4gu3.locky.ui.main.utils.toast

class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        //Set the support action bar to the toolbar
        setSupportActionBar(_binding.ToolbarLogin)

        darkModeVerification()

        setupNavigationUI()
    }

    private fun setupNavigationUI() {
        val navController = findNavController(R.id.Navigation_Host)
        NavigationUI.setupWithNavController(_binding.ToolbarLogin, navController)
    }

    private fun darkModeVerification() =
        when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.activateDarkStatusBar()
            Configuration.UI_MODE_NIGHT_NO -> window.activateLightAccentStatusBar(_binding.root)
            Configuration.UI_MODE_NIGHT_UNDEFINED -> window.activateLightAccentStatusBar(_binding.root)
            else -> toast(getString(R.string.error_internal_code_1))
        }
}
