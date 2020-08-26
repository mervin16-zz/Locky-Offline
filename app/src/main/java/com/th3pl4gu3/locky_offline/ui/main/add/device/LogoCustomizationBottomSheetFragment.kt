package com.th3pl4gu3.locky_offline.ui.main.add.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetDeviceLogoBinding
import com.th3pl4gu3.locky_offline.ui.main.add.LogoAccentAdapter
import com.th3pl4gu3.locky_offline.ui.main.add.LogoAccentClickListener
import com.th3pl4gu3.locky_offline.ui.main.add.LogoIconAdapter
import com.th3pl4gu3.locky_offline.ui.main.add.LogoIconClickListener
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.isNotInPortrait
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_DEVICE_LOGO_HEX
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_DEVICE_LOGO_ICON

class LogoCustomizationBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDeviceLogoBinding? = null
    private var _viewModel: LogoCustomizationBottomSheetViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetDeviceLogoBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(LogoCustomizationBottomSheetViewModel::class.java)
        /* Bind view model to layout */
        binding.viewModel = viewModel
        /* Get current icon and hex color */
        viewModel.icon =
            LogoCustomizationBottomSheetFragmentArgs.fromBundle(requireArguments()).iconcurrent
        viewModel.accent =
            LogoCustomizationBottomSheetFragmentArgs.fromBundle(requireArguments()).hexcurrent
        /* Bind lifecycle owner */
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        /*
        * We check if device is in landscape
        * If it is in landscape,
        * We expand the height of the bottom sheet
        */
        bottomSheetConfiguration()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Display the available icons
        */
        subscribeLogoIconList(viewModel.getAvailableIcons())

        /*
        * Display the available hex colors
        */
        subscribeLogoAccentList(viewModel.getAvailableHexColors())

        /*
        * Confirm button click listener
        */
        confirmClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeLogoIconList(icons: List<String>) {
        val adapter = LogoIconAdapter(
            LogoIconClickListener {
                viewModel.icon = it
            })

        binding.RecyclerViewLogoIcon.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }

        adapter.submitList(icons)
    }

    private fun subscribeLogoAccentList(hex: List<String>) {
        val adapter = LogoAccentAdapter(
            LogoAccentClickListener {
                viewModel.accent = it
            })

        binding.RecyclerViewLogoAccent.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }

        adapter.submitList(hex)
    }

    private fun bottomSheetConfiguration() {
        if (isNotInPortrait) {
            //This forces the sheet to appear at max height even on landscape
            BottomSheetBehavior.from(requireView().parent as View).state =
                BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun confirmClickListener() {
        binding.ButtonConfirmChanges.setOnClickListener {
            /* pass icon to parent fragment */
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                KEY_DEVICE_LOGO_ICON,
                viewModel.icon
            )
            /* Pass accent to parent fragment */
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                KEY_DEVICE_LOGO_HEX,
                viewModel.accent
            )
            dismiss()
        }
    }
}