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

class ViewCardActivity : AppCompatActivity() {

    private lateinit var _binding:ActivityViewCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_view_card)

        darkModeVerification()

        val card = (intent.extras!!["Card"]) as Card

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
                    action("Close") { dismiss() }
                }
            })

        _binding.RecyclerViewCredentialsField.adapter = credentialsAdapter

        return credentialsAdapter
    }

    private fun fieldList(card: Card): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(CredentialsField("Name on Card", card.cardHolderName, isCopyable = View.VISIBLE))
            add(CredentialsField("Bank", if(card.bank==null) "none" else card.bank!!, isCopyable = View.VISIBLE))
            add(CredentialsField("Pin", card.pin.toString(), View.VISIBLE, View.VISIBLE))
        }

    private fun copyToClipboardAndToast(message: String): Boolean {
        copyToClipboard(message)
        toast("Copied successfully")
        return true
    }
}
