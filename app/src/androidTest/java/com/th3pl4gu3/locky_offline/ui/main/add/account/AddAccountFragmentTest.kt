package com.th3pl4gu3.locky_offline.ui.main.add.account

import android.content.res.Resources
import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputLayout
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.TestUtil
import com.th3pl4gu3.locky_offline.core.credentials.Account
import com.th3pl4gu3.locky_offline.ui.main.LockyActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class AddAccountFragmentTest {

    @get:Rule
    var activityRule: ActivityTestRule<LockyActivity> = ActivityTestRule(
        LockyActivity::class.java
    )
    private var resources: Resources =
        InstrumentationRegistry.getInstrumentation().targetContext.resources

    @Before
    fun startActivity() {

        /*
        * Click the fab add button
        */
        onView(
            withId(R.id.FAB_Add)
        ).perform(click())
    }

    @Test
    fun testAddAccount_OnSubmitFormWithAllBlankFields_ErrorMessagesShouldAppear() {
        /*
        * We first need to scroll down to
        * get the button
        */
        onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

        /*
        * Then we click on the save button
        */
        onView(withId(R.id.Button_Save))
            .perform(click())

        /*
        * We then check if error messages appear
        */
        // Entry name error message
        onView(withId(R.id.Account_Name)).check(
            matches(
                hasTextInputLayoutErrorText(
                    resources.getString(
                        R.string.error_field_validation_blank
                    )
                )
            )
        )

        // Password error message
        onView(withId(R.id.Account_Password)).check(
            matches(
                hasTextInputLayoutErrorText(
                    resources.getString(
                        R.string.error_field_validation_blank
                    )
                )
            )
        )
    }

    @Test
    fun testAddAccount_OnSubmitFormWithEntryNameOnly_PasswordErrorMessagesShouldAppear() {

        val entryName = "Testing Account"

        /*
        * We enter a text for the entry name
        */
        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Name)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(entryName), closeSoftKeyboard())

        /*
        * We then need to scroll down to
        * get the button
        */
        onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

        /*
        * Then we click on the save button
        */
        onView(withId(R.id.Button_Save))
            .perform(click())

        /*
        * We then check if error messages appear
        */
        // Password error message
        onView(withId(R.id.Account_Password)).check(
            matches(
                hasTextInputLayoutErrorText(
                    resources.getString(
                        R.string.error_field_validation_blank
                    )
                )
            )
        )
    }

    @Test
    fun testAddAccount_OnSubmitFormWithWrongEmailFormat_EmailErrorMessageShouldShow() {

        val entryName = "Testing Account"
        val password = "Testing0123"
        val email = "invalidemail"

        /*
        * We enter a text for the entry name,email & password
        */
        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Name)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(entryName), closeSoftKeyboard())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Email)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(email), closeSoftKeyboard())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Password)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(password), closeSoftKeyboard())


        /*
        * We then need to scroll down to
        * get the button
        */
        onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

        /*
        * Then we click on the save button
        */
        onView(withId(R.id.Button_Save))
            .perform(click())

        /*
        * We then check if error messages appear
        */
        // email error message
        onView(withId(R.id.Account_Email)).check(
            matches(
                hasTextInputLayoutErrorText(
                    resources.getString(
                        R.string.error_field_validation_email_format
                    )
                )
            )
        )
    }

    @Test
    fun testAddAccount_OnSubmitFormWithRequiredFieldsOnly_FormShouldBeValid() {

        val entryName = "Testing Account"
        val password = "Testing0123"

        /*
        * We enter a text for the entry name & password
        */
        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Name)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(entryName), closeSoftKeyboard())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Password)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(password), closeSoftKeyboard())


        /*
        * We then need to scroll down to
        * get the button
        */
        onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

        /*
        * Then we click on the save button
        */
        onView(withId(R.id.Button_Save))
            .perform(click())

        /*
        * We then check if
        * fragment redirected to account fragment
        */
        onView(withId(R.id.Layout_Fragment_Account)).check(matches(isDisplayed()))

        /*
        * Then we check if toast appears correctly
        */
        onView(withText(resources.getString(R.string.message_credentials_created, entryName)))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAddThreeAccountsConsecutively_OnSubmitFormWithRequiredFieldsOnly_FormShouldBeValid() {

        var account: Account

        for (number in 1..3) {
            account = TestUtil.getAccount(number, "testing@emai.com")

            /*
            * We enter a text for the entry name & password
            */
            onView(
                allOf(
                    isDescendantOfA(withId(R.id.Account_Name)),
                    isAssignableFrom(EditText::class.java)
                )
            ).perform(typeText(account.entryName), closeSoftKeyboard())

            onView(
                allOf(
                    isDescendantOfA(withId(R.id.Account_Password)),
                    isAssignableFrom(EditText::class.java)
                )
            ).perform(typeText(account.password), closeSoftKeyboard())


            /*
            * We then need to scroll down to
            * get the button
            */
            onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

            /*
            * Then we click on the save button
            */
            onView(withId(R.id.Button_Save))
                .perform(click())

            /*
            * We then check if
            * fragment redirected to account fragment
            */
            onView(withId(R.id.Layout_Fragment_Account)).check(matches(isDisplayed()))

            /*
            * Then we check if toast appears correctly
            */
            onView(
                withText(
                    resources.getString(
                        R.string.message_credentials_created,
                        account.entryName
                    )
                )
            )
                .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
                .check(matches(isDisplayed()))


            if (number != 3) {
                /*
                * We then open add account again
                */
                onView(
                    withId(R.id.FAB_Add)
                ).perform(click())
            }
        }
    }

    @Test
    fun testAddAccount_OnSubmitFormWithAllFields_FormShouldBeValid() {

        val account = TestUtil.getAccount(1, "testing@email.com")

        /*
        * We enter a text for the fields
        */
        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Name)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(account.entryName), closeSoftKeyboard())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Username)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(account.username), closeSoftKeyboard())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Email)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(account.email), closeSoftKeyboard())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Password)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(account.password), closeSoftKeyboard())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Website)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(account.website), closeSoftKeyboard())

        /*
        * We then scroll down to
        * get more views
        */
        onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Auth)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(account.authenticationType), closeSoftKeyboard())

        /*
        * We then scroll down to
        * get more views
        */
        onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_2FA_Keys)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(account.twoFASecretKeys), closeSoftKeyboard())

        /*
        * We then scroll down to
        * get more views
        */
        onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

        onView(
            allOf(
                isDescendantOfA(withId(R.id.Account_Comments)),
                isAssignableFrom(EditText::class.java)
            )
        ).perform(typeText(account.additionalInfo), closeSoftKeyboard())

        /*
        * We then scroll down to
        * get more views
        */
        onView(withId(R.id.Nested_Scroll)).perform(swipeUp())

        /*
        * Then we click on the save button
        */
        onView(withId(R.id.Button_Save))
            .perform(click())

        /*
        * We then check if
        * fragment redirected to account fragment
        */
        onView(withId(R.id.Layout_Fragment_Account)).check(matches(isDisplayed()))

        /*
        * Then we check if toast appears correctly
        */
        onView(
            withText(
                resources.getString(
                    R.string.message_credentials_created,
                    account.entryName
                )
            )
        )
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }


    /*
    * Custom matcher to check error fields
    * in text input layout
    */
    private fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View?>? {
        return object : TypeSafeMatcher<View?>() {
            override fun describeTo(description: Description?) {}
            override fun matchesSafely(view: View?): Boolean {
                if (view !is TextInputLayout) {
                    return false
                }
                val error = view.error ?: return false
                val hint = error.toString()
                return expectedErrorText == hint
            }
        }
    }
}