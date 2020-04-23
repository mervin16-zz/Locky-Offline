package com.th3pl4gu3.locky.ui.main.add.account

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky.databinding.FragmentBottomSheetAccountLogoBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNT_LOGO


class LogoBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAccountLogoBinding? = null
    private lateinit var _viewModel: LogoBottomSheetViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetAccountLogoBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(LogoBottomSheetViewModel::class.java)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        with(binding) {
            ButtonCancel.setOnClickListener {
                dismiss()
            }

            TextfieldSearch.setOnEditorActionListener(OnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    TextfieldSearch.clearFocus()
                    (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(TextfieldSearch.windowToken, 0)
                    _viewModel.getWebsiteLogoProperties(TextfieldSearch.text.toString())
                    return@OnEditorActionListener true
                }
                false
            })
        }

        _viewModel.websites.observe(viewLifecycleOwner, Observer { logos ->
            if (logos != null) {
                if (logos.isEmpty()) {
                    binding.EmptyView.visibility = View.VISIBLE
                    binding.RecyclerViewLogo.visibility = View.GONE
                } else {
                    binding.EmptyView.visibility = View.GONE
                    binding.RecyclerViewLogo.visibility = View.VISIBLE
                    initiateLogoList().submitList(logos)
                    _viewModel.resetLogos()
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initiateLogoList(): LogoViewAdapter {
        val logoAdapter = LogoViewAdapter(
            ClickListener {
                //Pass logo url to parent fragment
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    KEY_ACCOUNT_LOGO,
                    it
                )
                dismiss()
            })

        binding.RecyclerViewLogo.apply {
            adapter = logoAdapter
            setHasFixedSize(true)
        }

        return logoAdapter
    }
}