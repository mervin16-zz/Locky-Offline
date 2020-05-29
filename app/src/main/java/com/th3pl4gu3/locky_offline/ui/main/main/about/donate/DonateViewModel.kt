package com.th3pl4gu3.locky_offline.ui.main.main.about.donate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.th3pl4gu3.locky_offline.R

class DonateViewModel(application: Application) : AndroidViewModel(application) {
    internal fun getDonations() = ArrayList<Donation>().apply {
        add(
            Donation(
                "Cookies",
                "\$10.00",
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!
            )
        )
        add(
            Donation(
                "Cookies",
                "\$10.00",
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!
            )
        )
        add(
            Donation(
                "Cookies",
                "\$10.00",
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!
            )
        )
        add(
            Donation(
                "Cookies",
                "\$10.00",
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!
            )
        )
        add(
            Donation(
                "Cookies",
                "\$10.00",
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!
            )
        )
        add(
            Donation(
                "Cookies",
                "\$10.00",
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!
            )
        )
        add(
            Donation(
                "Cookies",
                "\$10.00",
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!
            )
        )
        add(
            Donation(
                "Cookies",
                "\$10.00",
                getApplication<Application>().getDrawable(R.drawable.ic_rate)!!
            )
        )
    }
}