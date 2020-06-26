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
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.main.MainActivity


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


val FragmentManager.currentNavigationFragment: Fragment?
    get() = findFragmentById(R.id.Navigation_Host)?.childFragmentManager?.fragments?.first()

/**
 * Sets the exit and reenter transitions, or nulls them out if not provided.
 */
fun Fragment.setOutgoingTransitions(
    exitTransition: Any? = null,
    reenterTransition: Any? = null
) {
    this.exitTransition = exitTransition
    this.reenterTransition = reenterTransition
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
fun Fragment.requireMainActivity() = requireActivity() as MainActivity


/*
* Checks if screen is in landscape
*/
val Fragment.isNotInPortrait: Boolean
    get() = resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT