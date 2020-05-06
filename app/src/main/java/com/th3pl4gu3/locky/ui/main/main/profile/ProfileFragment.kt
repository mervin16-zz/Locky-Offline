package com.th3pl4gu3.locky.ui.main.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.exceptions.UserException
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.databinding.FragmentProfileBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants
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

        try {

            val user = getUser()
            binding.user = user

            initiateUserDetailsList().submitList(viewModel.fieldList(user))

        } catch (e: UserException) {
            navigateBack()
            toast(e.message!!)
        } catch (e: Exception) {
            navigateBack()
            toast(getString(R.string.error_internal_code_4, e.message!!))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun initiateUserDetailsList(): UserDetailsViewAdapter {
        val userDetailsViewAdapter = UserDetailsViewAdapter()

        binding.RecyclerViewUserDetails.apply {
            adapter = userDetailsViewAdapter
            setHasFixedSize(true)
        }

        return userDetailsViewAdapter
    }

    private fun getUser(): User {
        LocalStorageManager.with(requireActivity().application)
        return LocalStorageManager.get<User>(Constants.KEY_USER_ACCOUNT)
            ?: throw UserException(getString(R.string.error_internal_code_3))
    }

    private fun toast(message: String) = requireContext().toast(message)
}
