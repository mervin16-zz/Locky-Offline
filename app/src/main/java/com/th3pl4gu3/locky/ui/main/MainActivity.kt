package com.th3pl4gu3.locky.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.databinding.ActivityMainBinding
import com.th3pl4gu3.locky.ui.main.utils.activateDarkStatusBar
import com.th3pl4gu3.locky.ui.main.utils.activateLightStatusBar
import com.th3pl4gu3.locky.ui.main.utils.toast


class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val tabIcons = intArrayOf(
        R.drawable.ic_home,
        R.drawable.ic_account,
        R.drawable.ic_credit_card,
        R.drawable.ic_settings
    )

    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Bind the activity view.
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Update status bar upon current theme
        darkModeVerification()

        //Set toolbar as acton bar
        setSupportActionBar(_binding.ToolbarMain)

        /**
         * Setup for Tab layout, View Pager Adapter and Tab components
         **/
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        setupViewPagerFragments(sectionsPagerAdapter)

        _binding.ViewPagerMain.adapter = sectionsPagerAdapter
        _binding.TabLayoutMain.setupWithViewPager(_binding.ViewPagerMain)
        setupTabIcons()

        _binding.ViewPagerMain.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> _binding.FABAdd.show()
                    1 -> _binding.FABAdd.show()
                    2 -> _binding.FABAdd.show()
                    3 -> _binding.FABAdd.hide()
                }
            }
        })

        //Load expandable FABs and animations
        loadFABs()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_moreoptions_tab_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.Menu_Search -> {
            toast(getString(R.string.dev_feature_implementation_unknown, "Search"))
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun loadFABs(){
        val fabOpen = AnimationUtils.loadAnimation(this, R.anim.anim_fab_open)
        val fabClose = AnimationUtils.loadAnimation(this, R.anim.anim_fab_close)
        val fabClockwise = AnimationUtils.loadAnimation(this, R.anim.anim_fab_rotate_clockwise)
        val fabAnticlockwise = AnimationUtils.loadAnimation(this, R.anim.anim_fab_rotate_anticlockwise)

        _binding.FABAdd.setOnClickListener {

            isOpen = if(isOpen){
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
                toast(getString(R.string.dev_feature_implementation_unknown, "Account Creation"))
            }

            _binding.FABCard.setOnClickListener {
                toast(getString(R.string.dev_feature_implementation_unknown, "Card Creation"))
            }
        }
    }

    private fun darkModeVerification() = when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.activateDarkStatusBar()
            Configuration.UI_MODE_NIGHT_NO -> window.activateLightStatusBar(_binding.root)
            Configuration.UI_MODE_NIGHT_UNDEFINED -> window.activateLightStatusBar(_binding.root)
            else -> toast(getString(R.string.error_internal_code_1))
    }

    private fun setupViewPagerFragments(sectionsPagerAdapter: SectionsPagerAdapter) {
        sectionsPagerAdapter.addFragment(HomeFragment())
        sectionsPagerAdapter.addFragment(AccountFragment())
        sectionsPagerAdapter.addFragment(CardFragment())
        sectionsPagerAdapter.addFragment(SettingsFragment())
    }

    private fun setupTabIcons() = tabIcons.indices.forEach { x ->
        _binding.TabLayoutMain.getTabAt(x)!!.setIcon(tabIcons[x])
    }
}