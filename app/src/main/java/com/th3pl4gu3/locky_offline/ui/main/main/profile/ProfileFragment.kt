package com.th3pl4gu3.locky_offline.ui.main.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentProfileBinding
import com.th3pl4gu3.locky_offline.ui.main.main.MainActivity
import com.th3pl4gu3.locky_offline.ui.main.utils.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.toast

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

        /* Bind user details to layout*/
        binding.user = requireActivity().application.activeUser

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
                (requireActivity() as MainActivity).logout()

                /* Show a toast for the user */
                toast(getString(R.string.message_user_account_status_signed_out))
            }
        })
    }
}
