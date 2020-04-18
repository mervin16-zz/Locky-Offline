package com.th3pl4gu3.locky.ui.main.main

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.databinding.ActivityMainBinding
import com.th3pl4gu3.locky.ui.main.utils.activateDarkStatusBar
import com.th3pl4gu3.locky.ui.main.utils.activateLightStatusBar
import com.th3pl4gu3.locky.ui.main.utils.toast

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var _appBarConfiguration: AppBarConfiguration
    private var _isOpen = false
    private val _navigationFragments = setOf(
        R.id.Fragment_Home,
        R.id.Fragment_Card,
        R.id.Fragment_Account,
        R.id.Fragment_Device
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(_binding.ToolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Update status bar upon current theme
        darkModeVerification()

        //NavigationUI
        navigationUISetup()

        //Load expandable FABs and animations
        loadFABs()

        _binding.NestedScroll.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 0) {
                _binding.ToolbarMain.elevation = 8F
            } else {
                _binding.ToolbarMain.elevation = 0F
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(findNavController(R.id.Navigation_Host)) || super.onOptionsItemSelected(
            item
        )

    override fun onSupportNavigateUp() =
        findNavController(R.id.Navigation_Host).navigateUp(_appBarConfiguration)

    private fun darkModeVerification() =
        when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.activateDarkStatusBar()
            Configuration.UI_MODE_NIGHT_NO -> window.activateLightStatusBar(_binding.root)
            Configuration.UI_MODE_NIGHT_UNDEFINED -> window.activateLightStatusBar(_binding.root)
            else -> toast(getString(R.string.error_internal_code_1))
        }

    private fun navigationUISetup() {
        val navController = this.findNavController(R.id.Navigation_Host)
        _appBarConfiguration = AppBarConfiguration(_navigationFragments, _binding.DrawerMain)

        NavigationUI.setupActionBarWithNavController(this, navController, _appBarConfiguration)
        NavigationUI.setupWithNavController(_binding.NavigationView, navController)

        Navigation.setViewNavController(_binding.FABAccount, navController)
        Navigation.setViewNavController(_binding.FABCard, navController)

        navController.addOnDestinationChangedListener { nc, nd, _ ->
            when (nd.id) {
                nc.graph.startDestination,
                R.id.Fragment_Account,
                R.id.Fragment_Card,
                R.id.Fragment_Device -> {
                    _binding.DrawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    _binding.FABAdd.show()
                }
                else -> {
                    _binding.DrawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    //TODO: Fix FAB Hiding bug
                    _binding.FABAdd.hide()
                    _binding.FABAccount.hide()
                    _binding.FABCard.hide()
                }
            }
        }
    }

    private fun loadFABs(){
        val fabOpen = AnimationUtils.loadAnimation(this, R.anim.anim_fab_open)
        val fabClose = AnimationUtils.loadAnimation(this, R.anim.anim_fab_close)
        val fabClockwise = AnimationUtils.loadAnimation(this, R.anim.anim_fab_rotate_clockwise)
        val fabAnticlockwise = AnimationUtils.loadAnimation(this, R.anim.anim_fab_rotate_anticlockwise)

        _binding.FABAdd.setOnClickListener {

            _isOpen = if (_isOpen) {
                _binding.FABAdd.startAnimation(fabAnticlockwise)
                _binding.FABAccount.startAnimation(fabClose)
                _binding.FABCard.startAnimation(fabClose)
                false
            }else{
                _binding.FABAdd.startAnimation(fabClockwise)
                _binding.FABAccount.startAnimation(fabOpen)
                _binding.FABCard.startAnimation(fabOpen)

                true
            }

            _binding.FABAccount.setOnClickListener {
                findNavController(R.id.Navigation_Host).navigate(R.id.Fragment_Add_Account)
            }

            _binding.FABCard.setOnClickListener {
                toast(getString(R.string.dev_feature_implementation_unknown, "Card Creation"))
            }
        }
    }
}
