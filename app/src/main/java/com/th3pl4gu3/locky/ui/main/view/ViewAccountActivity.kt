package com.th3pl4gu3.locky.ui.main.view

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.ActivityViewAccountBinding
import com.th3pl4gu3.locky.ui.main.add.AddAccountActivity
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_NONE
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.VALUE_PARCELS_ACCOUNT

class ViewAccountActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityViewAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_view_account)

        darkModeVerification()

        val account = (intent.extras!![VALUE_PARCELS_ACCOUNT]) as Account

        _binding.account = account

        initiateCredentialsFieldList().submitList(fieldList(account))

        _binding.ButtonClose.setOnClickListener {
            finish()
        }

        _binding.ButtonAccountEdit.setOnClickListener{
            startActivity(Intent(this, AddAccountActivity::class.java).apply {
                putExtra("Account", account)
            })
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
                    action(getString(R.string.button_snack_action_close)) { dismiss() }
                }
            })

        _binding.RecyclerViewCredentialsField.adapter = credentialsAdapter

        return credentialsAdapter
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun fieldList(account: Account): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(CredentialsField(getString(R.string.field_account_username), if(account.username.isEmpty()) PLACEHOLDER_DATA_NONE else account.username, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_account_email), if(account.email.isEmpty()) PLACEHOLDER_DATA_NONE else account.email, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_account_password), account.password, View.VISIBLE, View.VISIBLE))
            add(CredentialsField(getString(R.string.field_account_website), if(account.website.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.website!!, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_account_2faauth), if(account.twoFA.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.twoFA!!))
            add(CredentialsField(getString(R.string.field_account_2fakeys), if(account.twoFASecretKeys.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.twoFASecretKeys!!))
            add(CredentialsField(getString(R.string.field_account_additional), if(account.additionalInfo.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.additionalInfo!!))
        }
}
