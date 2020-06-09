package com.th3pl4gu3.locky_offline.ui.main.add.account

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetAccountLogoBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_ACCOUNT_LOGO
import com.th3pl4gu3.locky_offline.ui.main.utils.isOnline
import com.th3pl4gu3.locky_offline.ui.main.utils.toast


class LogoBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAccountLogoBinding? = null
    private var _viewModel: LogoBottomSheetViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetAccountLogoBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(LogoBottomSheetViewModel::class.java)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //This forces the sheet to appear at max height even on landscape
        BottomSheetBehavior.from(requireView().parent as View).state =
            BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Cancel button listener
        binding.ButtonCancel.setOnClickListener {
            dismiss()
        }

        //Logos event
        observeLogosEvent()

        //Logo search text field listener
        textFieldSearchListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun textFieldSearchListener() {
        binding.TextfieldSearch.setOnEditorActionListener(OnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                dismissKeyBoardAndSearchForLogo()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun observeLogosEvent() {
        viewModel.websites.observe(viewLifecycleOwner, Observer { logos ->
            if (logos != null) {

                if (logos.isEmpty()) {
                    viewModel.setErrorLoadingStatus()
                    return@Observer
                }

                initiateLogoList().submitList(logos)
            }
        })
    }

    private fun dismissKeyBoardAndSearchForLogo() {
        with(binding) {
            TextfieldSearch.clearFocus()
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(TextfieldSearch.windowToken, 0)

            if (requireActivity().isOnline()) {
                viewModel?.getWebsiteLogoProperties(TextfieldSearch.text.toString())
            } else {
                toast(getString(R.string.message_internet_connection_unavailable))
            }
        }
    }

    private fun initiateLogoList(): LogoViewAdapter {
        val logoAdapter = LogoViewAdapter(
            ClickListener {
                passLogoUrlToParentFragmentAndDismiss(it)
            })

        binding.RecyclerViewLogo.apply {
            adapter = logoAdapter
            setHasFixedSize(true)
        }

        return logoAdapter
    }

    private fun passLogoUrlToParentFragmentAndDismiss(logoUrl: String) {
        //Pass logo url to parent fragment
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            KEY_ACCOUNT_LOGO,
            logoUrl
        )
        dismiss()
    }
}