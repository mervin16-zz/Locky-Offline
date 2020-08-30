package com.th3pl4gu3.locky_offline.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.ActivityLockyBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*

class LockyActivity : AppCompatActivity() {

    private lateinit var _appBarConfiguration: AppBarConfiguration
    private val binding: ActivityLockyBinding by contentView(R.layout.activity_locky)

    /*
    * Fragments listed here are
    * eligible for opening the navigation drawer
    * All other fragments not listed here will get the
    * back button instead of the hamburger menu icon
    * ONLY list fragments that can open the drawer menu here
    */
    private val _navigationFragments = setOf(
        R.id.Fragment_Card,
        R.id.Fragment_Account,
        R.id.Fragment_Bank_Account,
        R.id.Fragment_Device
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        /* Re set the style to the main theme app */
        setTheme(R.style.Locky_Theme)
        super.onCreate(savedInstanceState)

        /* Changes for the support action bar */
        lockyToolBarConfiguration(binding.ToolbarMain)

        /* Setup the JetPack Navigation UI */
        navigationUISetup()

        /* Load both FABs */
        listenerForFloatingActionButtons()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(
            item
        )

    override fun onSupportNavigateUp() = navController.navigateUp(_appBarConfiguration)

    /*
    * Private functions that are
    * called within the Locky Activity
    * itself.
    */

    private fun navigationUISetup() {
        //Fetch the Nav Controller
        with(navController) {
            //Setup the App Bar Configuration
            _appBarConfiguration = AppBarConfiguration(_navigationFragments, binding.DrawerMain)

            //Use Navigation UI to setup the app bar config and navigation view
            NavigationUI.setupActionBarWithNavController(
                this@LockyActivity,
                this,
                _appBarConfiguration
            )
            NavigationUI.setupWithNavController(binding.NavigationView, this)

            //Add on change destination listener to navigation controller to handle fab visibility
            navigationDestinationChangeListener(this)
        }
    }

    private fun navigationDestinationChangeListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, nd, _ ->

            // Update the toolbar title
            binding.ToolbarMainTitle.text = nd.label

            // Update UI according to navigation destination
            when (nd.id) {
                R.id.Fragment_Account,
                R.id.Fragment_Card,
                R.id.Fragment_Bank_Account,
                R.id.Fragment_Device -> {
                    binding.DrawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                    //Show all the FABs
                    showFABs()
                }
                else -> {
                    binding.DrawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                    //Hide all the FABs
                    hideFABs()
                }
            }
        }
    }

    private fun hideFABs() {
        binding.FABSearch.hide()
        binding.FABAdd.hide()
    }

    private fun showFABs() {
        binding.FABSearch.show()
        binding.FABAdd.show()
    }

    private fun listenerForFloatingActionButtons() {
        /* Listener for FAB Add */
        binding.FABAdd.setOnClickListener {
            navigateToAddScreenAccordingToCurrentFragment()
        }

        /* Listener for FAB Search */
        binding.FABSearch.setOnClickListener {
            navigateTo(R.id.action_global_Fragment_Search)
        }
    }
}
