package com.th3pl4gu3.locky_offline.ui.main.utils.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.LockyActivity

/*
* Locky Toolbar configuration
*/
fun AppCompatActivity.lockyToolBarConfiguration(toolbar: MaterialToolbar) {
    /* Set the default action bar to our custom material toolbar */
    setSupportActionBar(toolbar)

    /*
    * Remove the default left title on the toolbar
    * We will provide our own title centered in the middle
    */
    supportActionBar?.setDisplayShowTitleEnabled(false)
}

/*
* Returns the correct nav controller
* for the activity by automatically
* attaching the correct nav host
*/
inline val Activity.navController get() = findNavController(R.id.Navigation_Host)

/*
* Navigates to the corresponding add screen
* according to the current fragment the user is situated
* i.e a user in card fragment clicking on the add fab button
* will be redirected to the add card fragment
*/
fun Activity.navigateToAddScreenAccordingToCurrentFragment() {
    when (findNavController(R.id.Navigation_Host).currentDestination?.id) {
        R.id.Fragment_Account -> navigateTo(R.id.action_global_Fragment_Add_Account)
        R.id.Fragment_Card -> navigateTo(R.id.action_global_Fragment_Add_Card)
        R.id.Fragment_Bank_Account -> navigateTo(R.id.action_global_Fragment_Add_BankAccount)
        R.id.Fragment_Device -> navigateTo(R.id.action_global_Fragment_Add_Device)
    }
}

/*
* Checks if connected to the internet
*/
fun Activity.isOnline(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

/*
* JetPack navigation made easy
*/
fun Activity.navigateTo(destination: Int) {
    this.findNavController(R.id.Navigation_Host).navigate(destination)
}

/*
* Toast helper functions
*/
fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    with(Toast.makeText(requireContext(), text, duration)) {
        show()
    }
}

/*
* Copy to clipboard
* - can be called directly from a fragment
*/
fun Fragment.copyToClipboard(data: String) =
    (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
        ClipData.newPlainText(
            "text",
            data
        )
    )

/*
* Popup menu
*/
fun Fragment.createPopUpMenu(
    view: View,
    menu: Int,
    itemListener: PopupMenu.OnMenuItemClickListener,
    dismissListener: PopupMenu.OnDismissListener
) {
    val popup = PopupMenu(requireContext(), view)
    //inflating menu from xml resource
    popup.inflate(menu)
    //adding click & on dismiss listener
    popup.setOnMenuItemClickListener(itemListener)
    popup.setOnDismissListener(dismissListener)
    popup.show()
}

/*
* JetPack navigation made easy
*/
fun Fragment.navigateTo(destination: Int) {
    this.findNavController().navigate(destination)
}

fun Fragment.navigateTo(directions: NavDirections) {
    this.findNavController().navigate(directions)
}

/*
* Hides soft keyboard
*/
fun Fragment.hideSoftKeyboard(rootView: View) {
    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(rootView.windowToken, 0)
}

/*
* Converts an activity to the Main activity
* ATTENTION: Need to make sure that requireActivity() is
* actually returning MainActivity or application will crash
*/
fun Fragment.requireMainActivity() = requireActivity() as LockyActivity


/*
* Checks if screen is in landscape
*/
inline val Fragment.isNotInPortrait: Boolean
    get() = resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT