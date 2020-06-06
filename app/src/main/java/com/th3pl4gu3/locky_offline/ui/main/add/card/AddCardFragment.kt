package com.th3pl4gu3.locky_offline.ui.main.add.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.th3pl4gu3.locky_offline.databinding.FragmentAddCardBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.toast


class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private var _viewModel: AddCardViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(AddCardViewModel::class.java)

        //Bind view model and lifecycle owner
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //Fetch card if exists
        viewModel.setCard(AddCardFragmentArgs.fromBundle(requireArguments()).parcelcredcard)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Date picker listeners
         */
        datePickerListeners()

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun datePickerListeners() {
        with(binding) {
            IssuedDate.setOnClickListener {
                showIssuedDatePickerDialog()
            }

            ExpiryDate.setOnClickListener {
                showExpiryDatePickerDialog()
            }
        }
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
                binding.CardName.error = it
            })

            numberErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardNumber.error = it
            })

            pinErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardPin.error = it
            })

            bankErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardBank.error = it
            })

            cardHolderErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardHolder.error = it
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
                showToastAndNavigateToCardList(it)
            }
        })
    }

    private fun showToastAndNavigateToCardList(toastMessage: String) {
        toast(toastMessage)
        navigateTo(AddCardFragmentDirections.actionFragmentAddCardToFragmentCard())
    }

    private fun showIssuedDatePickerDialog() {
        val picker = MaterialDatePicker.Builder.datePicker().build()
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
        picker.addOnPositiveButtonClickListener { selection ->
            viewModel.updateIssuedDateText(selection)
        }

        picker.show(requireActivity().supportFragmentManager, picker.toString())
    }

    private fun showExpiryDatePickerDialog() {
        val picker = MaterialDatePicker.Builder.datePicker().build()
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
        picker.addOnPositiveButtonClickListener { selection ->
            viewModel.updateExpiryDateText(selection)
        }

        picker.show(requireActivity().supportFragmentManager, picker.toString())
    }

    private fun scrollToTop() {
        getParentScrollView().fling(0)
        getParentScrollView().smoothScrollTo(0, 0)
    }

    private fun getParentScrollView() = binding.root.parent.parent as NestedScrollView

    private fun toast(message: String) = requireContext().toast(message)
}
