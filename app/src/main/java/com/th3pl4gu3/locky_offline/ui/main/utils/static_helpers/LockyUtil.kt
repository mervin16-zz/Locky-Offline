package com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers

import android.app.SearchManager
import android.content.Intent
import android.net.Uri

object LockyUtil {

    /*
    * Locky intents that simplifies
    * opening implicit intents
    */
    // URL Opening Intent
    fun openUrl(url: String) = Intent(Intent.ACTION_WEB_SEARCH).apply {
        putExtra(SearchManager.QUERY, url)
    }

    // Email Opening Intent
    fun openMail(recipient: Array<String>, subject: String) = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(
            Intent.EXTRA_EMAIL,
            recipient
        ) // recipients
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }

    // Share Intent
    fun share(message: String) = Intent(Intent.ACTION_SEND).apply {
        putExtra(
            Intent.EXTRA_TEXT,
            message
        )
        type = "text/plain"
    }
}