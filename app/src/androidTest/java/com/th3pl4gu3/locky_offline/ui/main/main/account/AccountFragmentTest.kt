package com.th3pl4gu3.locky_offline.ui.main.main.account

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.LockyActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class AccountFragmentTest {

    @Before
    fun startActivity() {
        /*
        * Here we test if all the main fragments in the
        * MainActivity.kt works.
        * Any new main fragments added should be inserted in this
        * testing function
        */

        /*
        * We first launch the activity
        */
        ActivityScenario.launch(LockyActivity::class.java)
    }

    @Test
    fun testIfPageLoadsOnActivityStartup() {

        /*
        * Check if account fragment is
        * set as home fragment
        */
        onView(withId(R.id.Layout_Fragment_Account)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testSearchButton() {

        /*
        * Click the fab search button
        */
        onView(
            withId(R.id.FAB_Search)
        ).perform(ViewActions.click())

        /*
        * Check if search fragment is displayed
        */
        onView(withId(R.id.Layout_Parent_Search)).check(matches(ViewMatchers.isDisplayed()))

        /*
        * We navigate back and
        * check if we land on account fragment again
        */
        navigateUp()

        /*
       * Check if account fragment is displayed
       */
        onView(withId(R.id.Layout_Fragment_Account)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testAddButton() {

        /*
        * Click the fab add button
        */
        onView(
            withId(R.id.FAB_Add)
        ).perform(ViewActions.click())

        /*
        * Check if search fragment is displayed
        */
        onView(withId(R.id.Layout_Parent_Add_Account)).check(matches(ViewMatchers.isDisplayed()))

        /*
        * We navigate back and
        * check if we land on account fragment again
        */
        navigateUp()

        /*
       * Check if account fragment is displayed
       */
        onView(withId(R.id.Layout_Fragment_Account)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testFilterButton() {

        /*
        * Click the fab search button
        */
        onView(
            withId(R.id.Toolbar_Filter)
        ).perform(ViewActions.click())

        /*
        * Check if search fragment is displayed
        */
        onView(withId(R.id.Layout_Filter_Account)).check(matches(ViewMatchers.isDisplayed()))

        /*
        * We navigate back and
        * check if we land on account fragment again
        */
        onView(
            withId(R.id.Button_Changes_Confirm)
        ).perform(ViewActions.click())

        /*
       * Check if account fragment is displayed
       */
        onView(withId(R.id.Layout_Fragment_Account)).check(matches(ViewMatchers.isDisplayed()))
    }

    private fun openDrawer() = onView(withId(R.id.Drawer_Main))
        .check(matches(DrawerMatchers.isClosed(Gravity.START))) // Left Drawer should be closed.
        .perform(DrawerActions.open()) // Open Drawer

    private fun navigateUp() = onView(
        ViewMatchers.withContentDescription(
            R.string.abc_action_bar_up_description
        )
    ).perform(ViewActions.click())
}