package com.th3pl4gu3.locky.ui.main.view

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.ActivityViewCardBinding
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_NONE
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.VALUE_PARCELS_CARD

class ViewCardActivity : AppCompatActivity() {

    private lateinit var _binding:ActivityViewCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_view_card)

        darkModeVerification()

        val card = (intent.extras!![VALUE_PARCELS_CARD]) as Card

        _binding.card = card

        initiateCredentialsFieldList().submitList(fieldList(card))

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
                    action(getString(R.string.button_snack_action_close)) { dismiss() }
                }
            })

        _binding.RecyclerViewCredentialsField.adapter = credentialsAdapter

        return credentialsAdapter
    }

    private fun fieldList(card: Card): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(CredentialsField(getString(R.string.field_card_name), if(card.name.isEmpty()) PLACEHOLDER_DATA_NONE else card.name, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_bank), if(card.bank.isEmpty()) PLACEHOLDER_DATA_NONE else card.bank, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_pin), card.pin.toString(), View.VISIBLE, View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_date_issued), card.issuedDate.toFormattedString(), isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_date_expiry), card.expiryDate.toFormattedString(), isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_additional), if(card.additionalInfo.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else card.additionalInfo!!))
        }

    private fun copyToClipboardAndToast(message: String): Boolean {
        copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }
}
