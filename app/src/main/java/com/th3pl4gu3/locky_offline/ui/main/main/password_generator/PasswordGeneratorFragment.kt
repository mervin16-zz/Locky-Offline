package com.th3pl4gu3.locky_offline.ui.main.main.password_generator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentPasswordGeneratorBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.copyToClipboard
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast

class PasswordGeneratorFragment : Fragment() {

    /*
    * Private variables
    */
    private var _binding: FragmentPasswordGeneratorBinding? = null
    private var _viewModel: PasswordGeneratorViewModel? = null

    /*
    * Private properties
    */
    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    /*
    * Companion object
    */
    companion object {
        const val TAG = "PASSWORD_GENERATOR_FRAGMENT_DEBUG"
    }

    /*
    * Overridden methods
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Binds the UI */
        _binding = FragmentPasswordGeneratorBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(PasswordGeneratorViewModel::class.java)
        /* Bind view model to layout */
        binding.viewModel = _viewModel
        /* Bind lifecycle owner to this */
        binding.lifecycleOwner = this
        /* Returns the root view */
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        copyClickListener()
    }

    private fun copyClickListener() {
        binding.ButtonCopy.setOnClickListener {
            /* Copies the password to clipboard */
            copyToClipboard(viewModel.password)
            toast(getString(R.string.message_copy_successful))
        }
    }
}