package com.th3pl4gu3.locky.ui.main.add.account

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

        binding.go.setOnClickListener {
            _viewModel.loadLogos(binding.search.text.toString())
        }

        _viewModel.websites.observe(viewLifecycleOwner, Observer { logos ->
            if (logos != null) {
                if (logos.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.RecyclerViewLogo.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.RecyclerViewLogo.visibility = View.VISIBLE
                    initiateLogoList().submitList(logos)
                    _viewModel.resetLogos()
                }
            }
        })

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(binding.cl)
            behavior.skipCollapsed = true
            behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels;
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initiateLogoList(): LogoViewAdapter {
        val logoAdapter = LogoViewAdapter(
            ClickListener {
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