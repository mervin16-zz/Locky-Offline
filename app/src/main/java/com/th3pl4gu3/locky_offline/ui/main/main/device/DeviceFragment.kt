package com.th3pl4gu3.locky_offline.ui.main.main.device

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Device
import com.th3pl4gu3.locky_offline.core.main.DeviceSort
import com.th3pl4gu3.locky_offline.databinding.FragmentDeviceBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_DEVICE_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*

class DeviceFragment : Fragment() {

    /*
    * Private variables
    */
    private var _binding: FragmentDeviceBinding? = null
    private var _viewModel: DeviceViewModel? = null
    private var _lastClickTime: Long = 0

    /*
    * Private properties
    */
    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    /*
    * Companion object
    */
    companion object {
        const val TAG = "DEVICE_FRAGMENT_DEBUG"
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
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)
        /* Bind the view model to the layout */
        binding.viewModel = viewModel
        /* Bind lifecycle owner to this */
        binding.lifecycleOwner = this
        /* Returns the root view */
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Toolbar_Filter -> {
                navigateToSort()
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Hides the soft keyboard */
        hideSoftKeyboard(binding.root)

        /* Observe devices */
        observeDevices()

        /* Observe back stack entry for sort changes */
        observeBackStackEntryForSortSheet()
    }


    /*
    * Explicit private functions
    */
    private fun observeBackStackEntryForSortSheet() {
        val navController = findNavController()
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = navController.getBackStackEntry(R.id.Fragment_Device)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(KEY_DEVICE_SORT)
            ) {

                viewModel.sortChange(
                    navBackStackEntry.savedStateHandle.get<DeviceSort>(
                        KEY_DEVICE_SORT
                    )!!
                )

                navBackStackEntry.savedStateHandle.remove<DeviceSort>(KEY_DEVICE_SORT)
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

    private fun observeDevices() {
        viewModel.devices.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                /*
                 * If devices is not null
                 * Update the ui and
                 * Load recyclerview
                 */
                updateUI(it.size)

                subscribeDevices(it)
            }
        })
    }

    private fun updateUI(listSize: Int) {
        /*
        * Hide the loading animation
        * Alternate visibility between
        * Recyclerview & Empty View
        */
        viewModel.doneLoading(listSize)
    }

    private fun subscribeDevices(devices: List<Device>) {
        val adapter = DeviceAdapter(
            /* The click listener to handle device on clicks */
            ClickListener {
                navigateTo(DeviceFragmentDirections.actionFragmentDeviceToFragmentViewDevice(it))
            },
            /* The click listener to handle popup menu for each devices */
            OptionsClickListener { view, device ->
                view.apply {
                    isEnabled = false
                }
                createPopupMenu(view, device)
            },
            false
        )

        binding.RecyclerViewDevice.apply {
            /*
            * State that layout size will not change for better performance
            */
            setHasFixedSize(true)

            /* Bind the layout manager */
            layoutManager = LinearLayoutManager(requireContext())

            /* Bind the adapter */
            this.adapter = adapter
        }

        /* Submits the list for displaying */
        adapter.submitList(devices)
    }

    private fun createPopupMenu(view: View, device: Device) {
        createPopUpMenu(
            view,
            R.menu.menu_moreoptions_device,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyUsername -> copyToClipboardAndToast(device.username)
                    R.id.Menu_CopyPass -> copyToClipboardAndToast(device.password)
                    R.id.Menu_CopyIp -> copyToClipboardAndToast(device.ipAddress)
                    R.id.Menu_ShowPass -> showPasswordDialog(device.password)
                    else -> false
                }
            }, PopupMenu.OnDismissListener {
                view.apply {
                    isEnabled = true
                }
            })
    }

    private fun showPasswordDialog(password: String): Boolean {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                getString(
                    R.string.text_title_alert_showPassword
                )
            )
            .setMessage(
                password.setColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
            )
            .setPositiveButton(R.string.button_action_close) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        return true
    }

    private fun copyToClipboardAndToast(message: String?): Boolean {
        copyToClipboard(message ?: Constants.VALUE_EMPTY)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun navigateToSort() {
        if (SystemClock.elapsedRealtime() - _lastClickTime >= 800) {
            _lastClickTime = SystemClock.elapsedRealtime()
            navigateTo(DeviceFragmentDirections.actionFragmentDeviceToFragmentBottomDialogFilterDevice())
        }
    }
}
