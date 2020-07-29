package com.th3pl4gu3.locky_offline.ui.main.main

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.LockyActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class AppMainNavigationTest {


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
    fun testMainFragmentsInDrawer() {
        /*
        * Here we check if the
        * Home fragment is visible
        * The home fragment is the default fragment that shows up
        * when the activity starts
        * Here our home fragment is
        * [AccountFragment.kt]
        */
        onView(withId(R.id.Layout_Fragment_Account)).check(matches(isDisplayed()))

        /*
        * Fragment Card testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Card))

        onView(withId(R.id.Layout_Fragment_Card)).check(matches(isDisplayed()))

        /*
        * Fragment Bank Account testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Bank_Account))

        onView(withId(R.id.Layout_Fragment_Bank_Account)).check(matches(isDisplayed()))

        /*
        * Fragment Devices testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Device))

        onView(withId(R.id.Layout_Fragment_Device)).check(matches(isDisplayed()))
    }

    @Test
    fun testSubFragmentsInDrawer_UsingNavigationUpButton() {
        /*
        * We then navigate to a sub fragment
        * After navigating, we need to press the back button
        * Repeat for each sub fragments
        */

        /*
        * Profile Fragment Testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Profile))

        onView(withId(R.id.Layout_Fragment_Profile)).check(matches(isDisplayed()))

        navigateUp()

        /*
        * Settings Fragment Testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Settings))

        onView(withId(R.id.Toolbar_Main_Title)).check(matches(withText("Settings")))

        navigateUp()

        /*
        * About Fragment Testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_About))

        onView(withId(R.id.Layout_Fragment_About)).check(matches(isDisplayed()))

        // When the drawer is opened
        navigateUp()
    }

    @Test
    fun testSubFragmentsInDrawer_UsingBackButton() {
        /*
        * We then navigate to a sub fragment
        * After navigating, we need to press the back button
        * Repeat for each sub fragments
        */

        /*
        * Profile Fragment Testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Profile))

        onView(withId(R.id.Layout_Fragment_Profile)).check(matches(isDisplayed()))

        pressBack()

        /*
        * Settings Fragment Testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Settings))

        onView(withId(R.id.Toolbar_Main_Title)).check(matches(withText("Settings")))

        pressBack()

        /*
        * About Fragment Testing
        */
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_About))

        onView(withId(R.id.Layout_Fragment_About)).check(matches(isDisplayed()))

        // When the drawer is opened
        pressBack()
    }

    @Test
    fun testThatNavigatingThroughMultipleMainFragmentsAlwaysLandsOnAccountFragmentOnBackPressed() {
        /*
        * We navigate through all
        * main fragments
        * We then press back on the last fragment
        * Doing so should return us to the home fragment -> AccountFragment
        */
        // Card Fragment
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Card))

        onView(withId(R.id.Layout_Fragment_Card)).check(matches(isDisplayed()))

        // Bank Account Fragment
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Bank_Account))

        onView(withId(R.id.Layout_Fragment_Bank_Account)).check(matches(isDisplayed()))

        // Device Fragment
        openDrawer()

        onView(withId(R.id.Navigation_View))
            .perform(navigateTo(R.id.Fragment_Device))

        onView(withId(R.id.Layout_Fragment_Device)).check(matches(isDisplayed()))

        // We then press back
        pressBack()

        // Check if we land on account fragment
        onView(withId(R.id.Layout_Fragment_Account)).check(matches(isDisplayed()))
    }

    private fun openDrawer() = onView(withId(R.id.Drawer_Main))
        .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
        .perform(open()) // Open Drawer

    private fun navigateUp() = onView(
        withContentDescription(
            R.string.abc_action_bar_up_description
        )
    ).perform(click())
}
