package com.th3pl4gu3.locky_offline.ui.main.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
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
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.navigateTo

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var _viewModel: MainActivityViewModel
    private lateinit var _appBarConfiguration: AppBarConfiguration

    //Fragments that can navigate with the drawer
    private val _navigationFragments = setOf(
        R.id.Fragment_Card,
        R.id.Fragment_Account,
        R.id.Fragment_Bank_Account,
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

        /* Updates the app settings*/
        updateAppSettings()

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

    override fun finish() {
        super.finish()

        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

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
                _binding.ToolbarMain.elevation = 12F
            } else {
                _binding.ToolbarMain.elevation = 0F
            }
        }

    private fun navigationDestinationChangeListener_ToolbarTitle(navController: NavController) {
        navController.addOnDestinationChangedListener { _, nd, _ ->
            when (nd.id) {
                R.id.Fragment_Account -> updateToolbar(getString(R.string.text_title_screen_accounts))
                R.id.Fragment_Card -> updateToolbar(getString(R.string.text_title_screen_cards))
                R.id.Fragment_Bank_Account -> updateToolbar(getString(R.string.text_title_screen_bank_account))
                R.id.Fragment_Device -> updateToolbar(getString(R.string.text_title_screen_devices))
                R.id.Fragment_Settings -> updateToolbar(getString(R.string.text_title_screen_settings))
                R.id.Fragment_Profile -> updateToolbar(getString(R.string.text_title_screen_profile))
                R.id.Fragment_About -> updateToolbar(getString(R.string.text_title_screen_about))
                R.id.Fragment_Donate -> updateToolbar(getString(R.string.text_title_screen_donate))
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
                R.id.Fragment_Bank_Account,
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

    private fun getFadeNavOptions(): NavOptions? {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.anim_fade_in)
            .setExitAnim(R.anim.anim_fade_out)
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
        navigateToSearchFragment()
    }

    private fun updateAppSettings() {
        LocalStorageManager.withSettings(application)

        when (LocalStorageManager.get<String>(getString(R.string.settings_key_display_theme))) {
            getString(R.string.settings_value_display_default) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
            getString(R.string.settings_value_display_light) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            getString(R.string.settings_value_display_dark) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }
    }

    private fun navigateToSearchFragment() = findNavController(R.id.Navigation_Host).navigate(
        R.id.Fragment_Search, null, getFadeNavOptions()
    )

    private fun navigateToAddCategorySheet() = navigateTo(R.id.BottomSheet_Fragment_Add_Category)

    private fun navigateToSplashScreen() {
        navigateTo(R.id.Activity_Splash)
        finish()
    }

}
