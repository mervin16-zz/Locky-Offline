package com.th3pl4gu3.locky.ui.main.view

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.ActivityViewAccountBinding
import com.th3pl4gu3.locky.ui.main.main.card.CardOptionsClickListener
import com.th3pl4gu3.locky.ui.main.utils.*

class ViewAccountActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityViewAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_view_account)

        darkModeVerification()

        val account = (intent.extras!!["Account"]) as Account

        _binding.account = account

        initiateCredentialsFieldList().submitList(fieldList(account))

        _binding.ButtonClose.setOnClickListener {
            finish()
        }
    }

    private fun darkModeVerification() =
        when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.activateDarkStatusBar()
            Configuration.UI_MODE_NIGHT_NO -> window.activateLightStatusBar(_binding.root)
            Configuration.UI_MODE_NIGHT_UNDEFINED -> window.activateLightStatusBar(_binding.root)
            else -> toast(getString(R.string.error_internal_code_1))
        }

    private fun initiateCredentialsFieldList(): CredentialsViewAdapter {
        val credentialsAdapter = CredentialsViewAdapter(
            CopyClickListener { data ->
                copyToClipboardAndToast(data)
            },
            ViewClickListener { data ->
                _binding.LayoutCredentialView.snackbar(data) {
                    action("Close") { dismiss() }
                }
            })

        _binding.RecyclerViewCredentialsField.adapter = credentialsAdapter

        return credentialsAdapter
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        copyToClipboard(message)
        toast("Copied successfully")
        return true
    }

    private fun fieldList(account: Account): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(CredentialsField("Username", if(account.username==null) "none" else account.username!!, isCopyable = View.VISIBLE))
            add(CredentialsField("Email", if(account.email==null) "none" else account.email!!, isCopyable = View.VISIBLE))
            add(CredentialsField("Password", account.password, View.VISIBLE, View.VISIBLE))
            add(CredentialsField("Website", if(account.website==null) "none" else account.website!!, isCopyable = View.VISIBLE))
            add(CredentialsField("Is 2FA Authentication (Optional)", account.isTwoFA.toString()))
            add(CredentialsField("2FA Secret Keys (Optional)", if(account.twoFASecretKeys==null) "none" else account.twoFASecretKeys!!))
            add(CredentialsField("Additional Comments (Optional)", if(account.additionalInfo==null) "none" else account.additionalInfo!!))
        }
}
