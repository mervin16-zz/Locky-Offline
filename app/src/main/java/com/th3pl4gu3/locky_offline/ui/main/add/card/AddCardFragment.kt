package com.th3pl4gu3.locky_offline.ui.main.add.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.th3pl4gu3.locky_offline.databinding.FragmentAddCardBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast


class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private var _viewModel: AddCardViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Binds the UI */
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(AddCardViewModel::class.java)
        /* Bind view model to layout */
        binding.viewModel = viewModel
        /* Bind lifecycle owner to this */
        binding.lifecycleOwner = this

        /*
        * Fetch the key from argument
        * And set it to view model for fetching
        */
        viewModel.loadCard(
            AddCardFragmentArgs.fromBundle(requireArguments()).keycard,
            AddCardFragmentArgs.fromBundle(requireArguments()).keycardprevious
        )
        /* Returns the root view */
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

            cvcErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardCvc.error = it
            })

            bankErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardBank.error = it
            })

            cardHolderErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardHolder.error = it
            })

            issuedDateErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardIssuedDate.error = it
            })

            expiryDateErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.CardExpiryDate.error = it
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

    private fun scrollToTop() = with(binding.LayoutParentAddCard) {
        fling(0)
        smoothScrollTo(0, 0)
    }
}
