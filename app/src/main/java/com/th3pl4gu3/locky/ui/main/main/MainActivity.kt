package com.th3pl4gu3.locky.ui.main.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
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

    //Fragments that can navigate with the drawer
    private val _navigationFragments = setOf(
        R.id.Fragment_Home,
        R.id.Fragment_Card,
        R.id.Fragment_Account,
        R.id.Fragment_Device
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Set the support action bar to the toolbar
        setSupportActionBar(_binding.ToolbarMain)
        //Remove the default actionbar title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Update status bar upon current theme
        darkModeVerification()

        //Setup the navigation components
        navigationUISetup()

        //Load expandable FABs and animations
        loadFABs()

        //Scroll changes to adjust toolbar elevation accordingly
        _binding.NestedScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
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
        //Fetch the Nav Controller
        val navController = findNavController(R.id.Navigation_Host)
        //Setup the App Bar Configuration
        _appBarConfiguration = AppBarConfiguration(_navigationFragments, _binding.DrawerMain)

        //Use Navigation UI to setup the app bar config and navigation view
        NavigationUI.setupActionBarWithNavController(this, navController, _appBarConfiguration)
        NavigationUI.setupWithNavController(_binding.NavigationView, navController)

        //Set the mini FABs with navigation to navigate to fragments accordingly.
        Navigation.setViewNavController(_binding.FABAccount, navController)
        Navigation.setViewNavController(_binding.FABCard, navController)

        //Add on change destination listener to navigation controller
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

                    //Hide all the FABs
                    hideMiniFABs()
                    _binding.FABAdd.hide()
                }
            }
        }
    }

    private fun getSlideNavOptions(): NavOptions? {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.anim_slide_in_right)
            .setExitAnim(R.anim.anim_slide_out_left)
            .setPopEnterAnim(R.anim.anim_slide_in_left)
            .setPopExitAnim(R.anim.anim_slide_out_right)
            .build()
    }

    private fun loadFABs(){
        _binding.FABAdd.setOnClickListener {
            fabAction()
        }

        _binding.FABAccount.setOnClickListener {
            findNavController(R.id.Navigation_Host).navigate(
                R.id.Fragment_Add_Account,
                null,
                getSlideNavOptions()
            )
        }

        _binding.FABCard.setOnClickListener {
            findNavController(R.id.Navigation_Host).navigate(
                R.id.Fragment_Add_Card,
                null,
                getSlideNavOptions()
            )
        }
    }

    private fun fabAction() {
        if (_isOpen) {
            hideMiniFABs()
        } else {
            showMiniFABs()
        }
    }

    private fun hideMiniFABs() {
        _binding.FABAdd.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.anim_fab_rotate_anticlockwise
            )
        )
        _binding.FABAccount.hide()
        _binding.FABCard.hide()
        _isOpen = false
    }

    private fun showMiniFABs() {
        _binding.FABAdd.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.anim_fab_rotate_clockwise
            )
        )
        _binding.FABAccount.show()
        _binding.FABCard.show()

        _isOpen = true
    }
}
