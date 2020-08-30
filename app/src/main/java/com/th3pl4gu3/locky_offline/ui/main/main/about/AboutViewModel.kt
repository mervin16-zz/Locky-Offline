package com.th3pl4gu3.locky_offline.ui.main.main.about

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.th3pl4gu3.locky_offline.BuildConfig
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.applicationContext
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getString

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    /* The development list */
    internal fun getDevelopmentList() = ArrayList<AboutItem>().apply {
        add(
            AboutItem(
                getString(R.string.text_title_about_dev_rate),
                getString(R.string.text_description_about_dev_rate),
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_rate
                )!!,
                AboutItem.Item.DEV_RATE_US
            )
        )
        add(
            AboutItem(
                getString(R.string.text_title_about_dev_donate),
                getString(R.string.text_description_about_dev_donate),
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_donate
                )!!,
                AboutItem.Item.DEV_DONATE
            )
        )
        add(
            AboutItem(
                getString(R.string.text_title_about_dev_bug),
                getString(R.string.text_description_about_dev_bug),
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_bug_report
                )!!,
                AboutItem.Item.DEV_REPORT_BUG
            )
        )

        add(
            AboutItem(
                getString(R.string.text_title_about_dev_share),
                getString(R.string.text_description_about_dev_share),
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_share
                )!!,
                AboutItem.Item.DEV_SHARE
            )
        )
    }

    /* The others list */
    internal fun getOtherList() = ArrayList<AboutItem>().apply {
        add(
            AboutItem(
                getString(R.string.text_title_about_other_license),
                getString(R.string.text_title_about_other_license),
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_copyright
                )!!,
                AboutItem.Item.OTHER_LICENSES
            )
        )
        add(
            AboutItem(
                getString(R.string.text_title_about_other_developer),
                getString(R.string.text_description_about_other_developer),
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_developer
                )!!,
                AboutItem.Item.OTHER_DEVELOPER
            )
        )
        add(
            AboutItem(
                getString(R.string.text_title_about_other_terms),
                getString(R.string.text_title_about_other_terms),
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_policy
                )!!,
                AboutItem.Item.OTHER_POLICY
            )
        )
        add(
            AboutItem(
                getString(R.string.text_title_about_other_version),
                BuildConfig.VERSION_NAME,
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_version
                )!!,
                AboutItem.Item.OTHER_VERSION
            )
        )
    }

    /* The contributors list */
    internal fun getContributorsList() = ArrayList<Contributor>().apply {
        add(
            Contributor(
                name = getString(R.string.app_contributor_lead_name),
                description = getString(R.string.app_contributor_lead_description),
                icon = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_lead_developer
                )!!,
                url = getString(R.string.app_contributor_lead_url)

            )
        )
        add(
            Contributor(
                name = getString(R.string.app_contributor_qa_name),
                description = getString(R.string.app_contributor_qa_description),
                icon = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_quality_assurance
                )!!,
                url = getString(R.string.app_contributor_qa_url)
            )
        )

        add(
            Contributor(
                name = getString(R.string.app_contributor_graphic_name),
                description = getString(R.string.app_contributor_graphic_description),
                icon = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_graphic_designer
                )!!,
                url = getString(R.string.app_contributor_graphic_url)
            )
        )
    }
}