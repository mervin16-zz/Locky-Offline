package com.th3pl4gu3.locky.ui.main.main.about

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.th3pl4gu3.locky.BuildConfig
import com.th3pl4gu3.locky.R

class AboutViewModel(application: Application) : AndroidViewModel(application) {


    internal fun getDevelopmentList() = ArrayList<AboutItem>().apply {
        add(
            AboutItem(
                getApplication<Application>().getString(R.string.text_title_about_dev_rate),
                getApplication<Application>().getString(R.string.text_description_about_dev_rate),
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!,
                AboutItem.Item.DEV_RATE_US
            )
        )
        add(
            AboutItem(
                getApplication<Application>().getString(R.string.text_title_about_dev_donate),
                getApplication<Application>().getString(R.string.text_description_about_dev_donate),
                getApplication<Application>().getDrawable(R.drawable.ic_donate)!!,
                AboutItem.Item.DEV_DONATE
            )
        )
        add(
            AboutItem(
                getApplication<Application>().getString(R.string.text_title_about_dev_bug),
                getApplication<Application>().getString(R.string.text_description_about_dev_bug),
                getApplication<Application>().getDrawable(R.drawable.ic_bug_report)!!,
                AboutItem.Item.DEV_REPORT_BUG
            )
        )

        add(
            AboutItem(
                getApplication<Application>().getString(R.string.text_title_about_dev_share),
                getApplication<Application>().getString(R.string.text_description_about_dev_share),
                getApplication<Application>().getDrawable(R.drawable.ic_share)!!,
                AboutItem.Item.DEV_SHARE
            )
        )
    }

    internal fun getOtherList() = ArrayList<AboutItem>().apply {
        add(
            AboutItem(
                getApplication<Application>().getString(R.string.text_title_about_other_license),
                getApplication<Application>().getString(R.string.text_title_about_other_license),
                getApplication<Application>().getDrawable(R.drawable.ic_copyright)!!,
                AboutItem.Item.OTHER_LICENSES
            )
        )
        add(
            AboutItem(
                getApplication<Application>().getString(R.string.text_title_about_other_version),
                BuildConfig.VERSION_NAME,
                getApplication<Application>().getDrawable(R.drawable.ic_version)!!,
                AboutItem.Item.OTHER_VERSION
            )
        )
    }

}