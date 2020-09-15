package com.th3pl4gu3.locky_offline.ui.main.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.others.Statistic
import com.th3pl4gu3.locky_offline.databinding.FragmentProfileBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private var _viewModel: ProfileViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    private val _statistics = ArrayList<Statistic>()
    private val _adapter = StatisticsAdapter()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Observe Sign out event */
        observeSignOutEvent()

        /* Load the statistics */
        subscribeUi()

        /* Observe Account Size*/
        observeAccountSize()

        /* Observe Card Size*/
        observeCardsSize()

        /* Observe Bank Account Size*/
        observeBankAccountSize()

        /* Observe Device Size*/
        observeDeviceSize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    * Private Functions
    */
    private fun observeAccountSize() {
        viewModel.accountSize.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateStatistics(it)
            }
        })
    }

    private fun observeCardsSize() {
        viewModel.cardSize.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateStatistics(it)
            }
        })
    }

    private fun observeBankAccountSize() {
        viewModel.bankAccountSize.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateStatistics(it)
            }
        })
    }

    private fun observeDeviceSize() {
        viewModel.deviceSize.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateStatistics(it)
            }
        })
    }

    private fun subscribeUi() {
        binding.RecyclerViewStatistics.apply {
            /*
            * State that layout size will not change for better performance
            */
            setHasFixedSize(true)

            /* Bind the adapter */
            this.adapter = _adapter
        }
        _adapter.submitList(_statistics)
    }

    private fun updateStatistics(statistic: Statistic) {
        _statistics.apply {
            add(statistic)
            sortBy { it.name }
            _adapter.submitList(this)
        }
        _adapter.notifyDataSetChanged()
    }

    private fun observeSignOutEvent() {
        viewModel.signUserOut.observe(viewLifecycleOwner, Observer {
            if (it) {
                logout()
            }
        })
    }

    private fun logout() {
        /* Log the user out and clear session*/
        val mGoogleSignInClient = GoogleSignIn.getClient(
            requireContext().applicationContext, GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )

        mGoogleSignInClient.signOut()

        navigateTo(ProfileFragmentDirections.actionFragmentProfileToFragmentStarter())

        /* Show a toast for the user */
        toast(getString(R.string.message_user_account_status_signed_out))
    }
}
