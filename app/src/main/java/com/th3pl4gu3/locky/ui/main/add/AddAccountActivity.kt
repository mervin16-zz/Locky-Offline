package com.th3pl4gu3.locky.ui.main.add

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.ActivityAddAccountBinding
import com.th3pl4gu3.locky.ui.main.utils.activateDarkStatusBar
import com.th3pl4gu3.locky.ui.main.utils.activateLightStatusBar
import com.th3pl4gu3.locky.ui.main.utils.toast

class AddAccountActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityAddAccountBinding
    private lateinit var _viewModel: AddAccountViewModel
    private lateinit var _account: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_add_account)
        _viewModel = ViewModelProvider(this).get(AddAccountViewModel::class.java)

        //Set support action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Update status bar upon current theme
        darkModeVerification()

        //Bind view model and lifecycle owner
        _binding.viewModel = _viewModel
        _binding.lifecycleOwner = this

        //Fetch account if exists
        _account = if(intent.hasExtra("Account")) ((intent.extras!!["Account"]) as Account) else Account()
        _viewModel.setAccount(_account)

        _binding.ButtonSave.setOnClickListener {
            _viewModel.isFormValid(
                _account.apply {
                    name = _binding.AccountName.editText?.text.toString()
                    username = _binding.AccountUsername.editText?.text.toString()
                    email = _binding.AccountEmail.editText?.text.toString()
                    password = _binding.AccountPassword.editText?.text.toString()
                }
            )
        }

        /** Other Observations**/
        _viewModel.toastEvent.observe(this, Observer {
            if(it != null){
                toast(it)
                _viewModel.doneWithToastEvent()
            }
        })

        /** Form Validation Observations**/
        _viewModel.nameErrorMessage.observe(this, Observer {
            _binding.AccountName.error = it
        })

        _viewModel.usernameErrorMessage.observe(this, Observer {
            _binding.AccountUsername.error = it
        })

        _viewModel.emailErrorMessage.observe(this, Observer {
            _binding.AccountEmail.error = it
        })

        _viewModel.passwordErrorMessage.observe(this, Observer {
            _binding.AccountPassword.error = it
        })

        _viewModel.isFormValid.observe(this, Observer {
            if (it) {
                //TODO: Add database code here to insert account
                toast("Credentials has been stored.")
                finish()
            }
        })
    }

    private fun darkModeVerification() =
        when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.activateDarkStatusBar()
            Configuration.UI_MODE_NIGHT_NO -> window.activateLightStatusBar(_binding.root)
            Configuration.UI_MODE_NIGHT_UNDEFINED -> window.activateLightStatusBar(_binding.root)
            else -> toast(getString(R.string.error_internal_code_1))
        }
}
