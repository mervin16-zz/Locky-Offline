package com.th3pl4gu3.locky.ui.main.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.exceptions.UserException
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.databinding.FragmentProfileBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky.ui.main.utils.toast

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private var _viewModel: ProfileViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        /*
        * Fetch the user details from session
        */
        val user = getUser()

        /* Bind user details to layout*/
        binding.user = user

        /* Bind view model to layout*/
        binding.viewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Observe Sign out event */
        observeSignOutEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeSignOutEvent() {
        viewModel.signUserOut.observe(viewLifecycleOwner, Observer {
            if (it) {
                /* Log the user out from firebase and clear session*/
                logout()

                /* Show a toast for the user */
                toast(getString(R.string.message_user_account_status_signed_out))
            }
        })
    }

    private fun getUser(): User {
        LocalStorageManager.with(requireActivity().application)
        return LocalStorageManager.get<User>(KEY_USER_ACCOUNT)
            ?: throw UserException(getString(R.string.error_internal_code_3))
    }

    private fun logout() {
        AuthUI.getInstance().signOut(requireContext())

        LocalStorageManager.with(requireActivity().application)
        LocalStorageManager.remove(KEY_USER_ACCOUNT)
    }

    private fun toast(message: String) = requireContext().toast(message)
}
