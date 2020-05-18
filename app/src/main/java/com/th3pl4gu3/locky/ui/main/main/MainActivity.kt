package com.th3pl4gu3.locky.ui.main.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.databinding.ActivityMainBinding
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState


class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var _viewModel: MainActivityViewModel
    private lateinit var _appBarConfiguration: AppBarConfiguration

    //Fragments that can navigate with the drawer
    private val _navigationFragments = setOf(
        R.id.Fragment_Card,
        R.id.Fragment_Account,
        R.id.Fragment_Device
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        _viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        _binding.lifecycleOwner = this

        //Set the support action bar to the toolbar
        setSupportActionBar(_binding.ToolbarMain)
        //Remove the default actionbar title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Observer to check if user has been authenticated
        observeAuthenticationState()

        //Update status bar upon current theme
        //darkModeVerification()

        //Setup the navigation components
        navigationUISetup()

        //Load FABs
        listenerForAddFab()

        listenerForSearchFab()

        //Scroll changes to adjust toolbar elevation accordingly
        setUpNestedScrollChangeListener()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(findNavController(R.id.Navigation_Host)) || super.onOptionsItemSelected(
            item
        )

    override fun onSupportNavigateUp() =
        findNavController(R.id.Navigation_Host).navigateUp(_appBarConfiguration)

    /*private fun darkModeVerification() =
        when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.activateDarkStatusBar()
            Configuration.UI_MODE_NIGHT_NO -> window.activateLightStatusBar(_binding.root)
            Configuration.UI_MODE_NIGHT_UNDEFINED -> window.activateLightStatusBar(_binding.root)
            else -> toast(getString(R.string.error_internal_code_2))
        }*/

    private fun navigationUISetup() {
        //Fetch the Nav Controller
        val navController = findNavController(R.id.Navigation_Host)
        //Setup the App Bar Configuration
        _appBarConfiguration = AppBarConfiguration(_navigationFragments, _binding.DrawerMain)

        //Use Navigation UI to setup the app bar config and navigation view
        NavigationUI.setupActionBarWithNavController(this, navController, _appBarConfiguration)
        NavigationUI.setupWithNavController(_binding.NavigationView, navController)

        //Set the mini FABs with navigation to navigate to fragments accordingly.
        Navigation.setViewNavController(_binding.FABAdd, navController)
        Navigation.setViewNavController(_binding.FABSearch, navController)

        //Add on change destination listener to navigation controller to handle fab visibility
        navigationDestinationChangeListener_FAB(navController)

        //Add on change destination listener to navigation controller to handle screen title visibility
        navigationDestinationChangeListener_ToolbarTitle(navController)
    }

    private fun setUpNestedScrollChangeListener() =
        _binding.NestedScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0) {
                _binding.ToolbarMain.elevation = 8F
            } else {
                _binding.ToolbarMain.elevation = 0F
            }
        }

    private fun navigationDestinationChangeListener_ToolbarTitle(navController: NavController) {
        navController.addOnDestinationChangedListener { nc, nd, _ ->
            when (nd.id) {
                R.id.Fragment_Account -> updateToolbar("Accounts")
                R.id.Fragment_Card -> updateToolbar("Cards")
                R.id.Fragment_Device -> updateToolbar("Devices")
                R.id.Fragment_Settings -> updateToolbar("Settings")
                R.id.Fragment_Profile -> updateToolbar("Profile")
                R.id.Fragment_About -> updateToolbar("About")
                else -> {
                    //Show the toolbar
                    updateToolbar(null)
                }
            }
        }
    }

    private fun navigationDestinationChangeListener_FAB(navController: NavController) {
        navController.addOnDestinationChangedListener { nc, nd, _ ->
            when (nd.id) {
                nc.graph.startDestination,
                R.id.Fragment_Card,
                R.id.Fragment_Device -> {
                    _binding.DrawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                    //Show all the FABs
                    showFABs()
                }
                else -> {
                    _binding.DrawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                    //Hide all the FABs
                    hideFABs()
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

    private fun updateToolbar(title: String?) {
        if (title != null) {
            updateToolbarTitle(title)
            toolbarTitleVisibility(true)
        } else {
            toolbarTitleVisibility(false)
        }
    }

    private fun updateToolbarTitle(title: String) {
        _binding.ToolbarMainTitle.text = title
    }

    private fun toolbarTitleVisibility(visibility: Boolean) {
        _binding.ToolbarMainTitle.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    private fun hideFABs() {
        _binding.FABSearch.hide()
        _binding.FABAdd.hide()
    }

    private fun showFABs() {
        _binding.FABSearch.show()
        _binding.FABAdd.show()

        showFABFromSlidingBehavior(_binding.FABSearch, _binding.FABSearch.isVisible)
        showFABFromSlidingBehavior(_binding.FABAdd, _binding.FABAdd.isVisible)
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

    private fun listenerForAddFab() = _binding.FABAdd.setOnClickListener {
            navigateToAddCategorySheet()
        }

    private fun listenerForSearchFab() = _binding.FABSearch.setOnClickListener {
            navigateToSearchSheet()
        }

    private fun observeAuthenticationState() =
        _viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationState.UNAUTHENTICATED -> {
                    navigateToSplashScreen()
                }
                else -> return@Observer
            }
        })

    private fun navigateToSearchSheet() = findNavController(R.id.Navigation_Host).navigate(
            R.id.BottomSheet_Fragment_Search, null
        )

    private fun navigateToAddCategorySheet() = findNavController(R.id.Navigation_Host).navigate(
            R.id.BottomSheet_Fragment_Add_Category,
            null
        )

    private fun navigateToSplashScreen() =
        findNavController(R.id.Navigation_Host).navigate(R.id.Activity_Splash)
}
