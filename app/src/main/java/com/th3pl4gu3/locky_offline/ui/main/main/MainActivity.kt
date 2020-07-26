package com.th3pl4gu3.locky_offline.ui.main.main

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.ActivityMainBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo

class MainActivity : AppCompatActivity() {

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

        //Scroll changes to adjust toolbar elevation accordingly
        setUpNestedScrollChangeListener()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(findNavController(R.id.Navigation_Host)) || super.onOptionsItemSelected(
            item
        )

    override fun onSupportNavigateUp() =
        findNavController(R.id.Navigation_Host).navigateUp(_appBarConfiguration)

    internal fun logout() {
        val mGoogleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )

        mGoogleSignInClient.signOut()

        navigateToSplashScreen()
    }

    private fun navigationUISetup() {
        //Fetch the Nav Controller
        val navController = findNavController(R.id.Navigation_Host)
        //Setup the App Bar Configuration
        _appBarConfiguration = AppBarConfiguration(_navigationFragments, binding.DrawerMain)

        //Use Navigation UI to setup the app bar config and navigation view
        NavigationUI.setupActionBarWithNavController(this, navController, _appBarConfiguration)
        NavigationUI.setupWithNavController(binding.NavigationView, navController)

        //Add on change destination listener to navigation controller to handle fab visibility
        navigationDestinationChangeListener(navController)
    }

    private fun setUpNestedScrollChangeListener() =
        binding.NestedScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0) {
                binding.ToolbarMain.elevation = 18F
            } else {
                binding.ToolbarMain.elevation = 0F
            }
        }

    private fun navigationDestinationChangeListener(navController: NavController) {
        navController.addOnDestinationChangedListener { nc, nd, _ ->

            // Update the toolbar title
            updateToolBarTitle(nd)

            // Update UI according to navigation destination
            when (nd.id) {
                nc.graph.startDestination,
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

        showFABFromSlidingBehavior(binding.FABSearch, binding.FABSearch.isVisible)
        showFABFromSlidingBehavior(binding.FABAdd, binding.FABAdd.isVisible)
    }

    private fun showFABFromSlidingBehavior(fab: FloatingActionButton, isVisible: Boolean) {
        val layoutParams: ViewGroup.LayoutParams = fab.layoutParams
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            val behavior = layoutParams.behavior
            if (behavior is HideBottomViewOnScrollBehavior) {
                if (isVisible) {
                    behavior.slideUp(fab)
                } else {
                    behavior.slideDown(fab)
                }
            }
        }
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

    private fun navigateToSplashScreen() {
        /* Returns to the Splash Screen */
        navigateTo(R.id.Activity_Splash)
        finish()
    }

}
