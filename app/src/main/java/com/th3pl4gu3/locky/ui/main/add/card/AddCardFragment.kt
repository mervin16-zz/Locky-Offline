package com.th3pl4gu3.locky.ui.main.add.card

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.databinding.FragmentAddCardBinding
import com.th3pl4gu3.locky.ui.main.utils.toast
import java.util.*


class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private var _viewModel: AddCardViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    private var _issuedDate: OnDateSetListener = OnDateSetListener { _, year, month, _ ->
        viewModel.updateIssuedDateText(month, year)
    }

    private var _expiryDate: OnDateSetListener = OnDateSetListener { _, year, month, _ ->
        viewModel.updateExpiryDateText(month, year)
    }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun datePickerListeners() {
        with(binding) {
            IssuedDate.setOnClickListener {
                showDatePickerDialog(_issuedDate)
            }

            ExpiryDate.setOnClickListener {
                showDatePickerDialog(_expiryDate)
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

    private fun observeFormValidityEvent() {
        viewModel.formValidity.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToastAndNavigateToCardList(it)
            }
        })
    }

    private fun showToastAndNavigateToCardList(toastMessage: String) {
        toast(getString(R.string.message_credentials_created, toastMessage))
        findNavController().navigate(AddCardFragmentDirections.actionFragmentAddCardToFragmentCard())
    }

    private fun showDatePickerDialog(dateSetListener: OnDateSetListener) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH)
        ).show()
    }

    private fun toast(message: String) = requireContext().toast(message)
}
