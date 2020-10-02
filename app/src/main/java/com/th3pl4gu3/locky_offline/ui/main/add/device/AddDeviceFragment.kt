package com.th3pl4gu3.locky_offline.ui.main.add.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentAddDeviceBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_DEVICE_LOGO_HEX
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_DEVICE_LOGO_ICON


class AddDeviceFragment : Fragment() {

    private var _binding: FragmentAddDeviceBinding? = null
    private var _viewModel: AddDeviceViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Binds the UI */
        _binding = FragmentAddDeviceBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(AddDeviceViewModel::class.java)
        /* Bind view model to layout */
        binding.viewModel = viewModel
        /* Bind lifecycle owner to this */
        binding.lifecycleOwner = this

        /*
        * Fetch the key from argument
        * And set it to view model for fetching
        */
        viewModel.loadDevice(
            AddDeviceFragmentArgs.fromBundle(requireArguments()).keydevice,
            AddDeviceFragmentArgs.fromBundle(requireArguments()).keydeviceprevious
        )
        /* Returns the root view */
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Change logo accent click listener
        */
        logoAccentClickListener()

        /**
         * Form validity event
         */
        observeFormValidityEvent()

        /**
         * Form validation error messages events
         */
        observeFormValidationErrorMessagesEvents()

        /**
         * Observe toast event
         */
        observeToastEvent()

        /* Observe if form has errors*/
        observeIfHasErrors()

        /*
        * Observe back stack entry for
        * accent color changes in logo
        */
        observeBackStackEntryForLogoAccent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeToastEvent() {
        viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toast(it)
                viewModel.doneWithToastEvent()
            }
        })
    }

    private fun observeFormValidationErrorMessagesEvents() {
        with(viewModel) {
            nameErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.DeviceEntryName.error = it
            })

            usernameErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.DeviceUsername.error = it
            })

            passwordErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.DevicePassword.error = it
            })
        }
    }

    private fun observeIfHasErrors() {
        viewModel.hasErrors.observe(viewLifecycleOwner, Observer {
            if (it) {
                scrollToTop()
                viewModel.resetErrorsFlag()
            }
        })
    }

    private fun observeFormValidityEvent() {
        viewModel.formValidity.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToastAndNavigateToDeviceList(it)
            }
        })
    }

    private fun observeBackStackEntryForLogoAccent() {
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.Fragment_Add_Device)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(KEY_DEVICE_LOGO_HEX) || navBackStackEntry.savedStateHandle.contains(
                    KEY_DEVICE_LOGO_ICON
                )
            ) {
                /*
                * Update the icon & accent color
                */
                viewModel.icon =
                    navBackStackEntry.savedStateHandle.get<String>(KEY_DEVICE_LOGO_ICON)!!

                viewModel.accent =
                    navBackStackEntry.savedStateHandle.get<String>(KEY_DEVICE_LOGO_HEX)!!

                //Remove the saved data
                navBackStackEntry.savedStateHandle.remove<String>(KEY_DEVICE_LOGO_HEX)
                navBackStackEntry.savedStateHandle.remove<String>(KEY_DEVICE_LOGO_ICON)
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun logoAccentClickListener() {
        binding.DeviceLogoEdit.setOnClickListener {
            navigateTo(
                AddDeviceFragmentDirections.actionFragmentAddDeviceToFragmentBottomDialogLogoDevice(
                    viewModel.icon
                ).setHEXCURRENT(viewModel.accent)
            )
        }
    }

    private fun showToastAndNavigateToDeviceList(toastMessage: String) {
        toast(toastMessage)
        navigateTo(AddDeviceFragmentDirections.actionFragmentAddDeviceToFragmentDevice())
    }

    private fun scrollToTop() = with(binding.LayoutParentAddDevice) {
        fling(0)
        smoothScrollTo(0, 0)
    }
}
