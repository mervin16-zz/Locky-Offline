package com.th3pl4gu3.locky_offline.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.ActivityMainBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo

class LockyActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private lateinit var _appBarConfiguration: AppBarConfiguration

    private val binding get() = _binding!!

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
        /* Set binding to the xml layout */
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        /* Set the default action bar to our custom material toolbar */
        setSupportActionBar(binding.ToolbarMain)

        /*
        * Remove the default left title on the toolbar
        * We will provide our own title centered in the middle
        */
        supportActionBar?.setDisplayShowTitleEnabled(false)

        /* Setup the JetPack Navigation UI */
        navigationUISetup()

        /* Load both FABs */
        listenerForFloatingActionButtons()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(findNavController(R.id.Navigation_Host)) || super.onOptionsItemSelected(
            item
        )

    override fun onSupportNavigateUp() =
        findNavController(R.id.Navigation_Host).navigateUp(_appBarConfiguration)

    /*
    * Private functions that are
    * called within the Locky Activity
    * Itself.
    */
    private fun navigationUISetup() {
        //Fetch the Nav Controller
        with(findNavController(R.id.Navigation_Host)) {
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
        navController.addOnDestinationChangedListener { nc, nd, _ ->

            // Update the toolbar title
            updateToolBarTitle(nd)

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

    private fun updateToolBarTitle(navigationDestination: NavDestination) {
        binding.ToolbarMainTitle.text = navigationDestination.label
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

    private fun navigateToAddScreenAccordingToCurrentFragment() {
        /*
        * Navigates to the corresponding add screen
        * according to the current fragment
        * the user is situated
        * i.e a user in card fragment clicking on the add fab button
        * will be redirected to the add card fragment
        */
        when (findNavController(R.id.Navigation_Host).currentDestination?.id) {
            R.id.Fragment_Account -> navigateTo(R.id.action_global_Fragment_Add_Account)
            R.id.Fragment_Card -> navigateTo(R.id.action_global_Fragment_Add_Card)
            R.id.Fragment_Bank_Account -> navigateTo(R.id.action_global_Fragment_Add_BankAccount)
            R.id.Fragment_Device -> navigateTo(R.id.action_global_Fragment_Add_Device)
        }
    }
}
