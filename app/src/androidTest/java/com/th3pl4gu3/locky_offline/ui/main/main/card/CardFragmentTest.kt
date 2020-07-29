package com.th3pl4gu3.locky_offline.ui.main.main.card

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.LockyActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class CardFragmentTest {

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

        /*
        * We then navigate to cards fragment on startup
        */
        openDrawer()

        onView(ViewMatchers.withId(R.id.Navigation_View))
            .perform(NavigationViewActions.navigateTo(R.id.Fragment_Card))

        onView(ViewMatchers.withId(R.id.Layout_Fragment_Card)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun testSearchButton() {
        /*
        * Click the fab search button
        */
        onView(
            ViewMatchers.withId(R.id.FAB_Search)
        ).perform(ViewActions.click())

        /*
        * Check if search fragment is displayed
        */
        onView(ViewMatchers.withId(R.id.Layout_Parent_Search))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        /*
        * We navigate back and
        * check if we land on cards fragment again
        */
        navigateUp()

        /*
       * Check if card fragment is displayed
       */
        onView(ViewMatchers.withId(R.id.Layout_Fragment_Card))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testAddButton() {

        /*
        * Click the fab add button
        */
        onView(
            ViewMatchers.withId(R.id.FAB_Add)
        ).perform(ViewActions.click())

        /*
        * Check if add fragment is displayed
        */
        onView(ViewMatchers.withId(R.id.Layout_Parent_Add_Card))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        /*
        * We navigate back and
        * check if we land on cards fragment again
        */
        navigateUp()

        /*
       * Check if card fragment is displayed
       */
        onView(ViewMatchers.withId(R.id.Layout_Fragment_Card))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testFilterButton() {

        /*
        * Click the fab search button
        */
        onView(
            ViewMatchers.withId(R.id.Toolbar_Filter)
        ).perform(ViewActions.click())

        /*
        * Check if search fragment is displayed
        */
        onView(ViewMatchers.withId(R.id.Layout_Filter_Card)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )

        /*
        * We navigate back and
        * check if we land on account fragment again
        */
        onView(
            ViewMatchers.withId(R.id.Button_Changes_Confirm)
        ).perform(ViewActions.click())

        /*
       * Check if account fragment is displayed
       */
        onView(ViewMatchers.withId(R.id.Layout_Fragment_Card)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun testBackButton() {

        /*
        * On back press
        * we need to go on account fragment
        */
        pressBack()

        /*
        * Check if account fragment is displayed
        */
        onView(ViewMatchers.withId(R.id.Layout_Fragment_Account)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    private fun openDrawer() = onView(ViewMatchers.withId(R.id.Drawer_Main))
        .check(ViewAssertions.matches(DrawerMatchers.isClosed(Gravity.START))) // Left Drawer should be closed.
        .perform(DrawerActions.open()) // Open Drawer

    private fun navigateUp() = onView(
        ViewMatchers.withContentDescription(
            R.string.abc_action_bar_up_description
        )
    ).perform(ViewActions.click())
}