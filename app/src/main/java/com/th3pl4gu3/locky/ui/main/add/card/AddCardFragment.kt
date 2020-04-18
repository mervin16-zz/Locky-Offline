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
import androidx.navigation.findNavController
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.FragmentAddCardBinding
import com.th3pl4gu3.locky.ui.main.utils.toast
import java.util.*


class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private lateinit var _viewModel: AddCardViewModel
    private lateinit var _card: Card

    private val binding get() = _binding!!

    var issuedDate: OnDateSetListener = OnDateSetListener { _, year, month, _ ->
        updateIssuedDateText(month, year)
    }

    var expiryDate: OnDateSetListener = OnDateSetListener { _, year, month, _ ->
        updateExpiryDateText(month, year)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)

        _viewModel = ViewModelProvider(this).get(AddCardViewModel::class.java)

        //Bind view model and lifecycle owner
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this


        //Fetch account if exists
        _card = Card()
        _viewModel.setCard(_card)

        binding.ButtonSave.setOnClickListener {
            _viewModel.isFormValid(
                _card.apply {
                    name = binding.CardName.editText?.text.toString()
                    number = binding.CardNumber.editText?.text.toString().toLong()
                    pin = binding.CardPin.editText?.text.toString().toShort()
                    bank = binding.CardBank.editText?.text.toString()
                    cardHolderName = binding.CardHolder.editText?.text.toString()
                    //TODO: Add Date variables
                    additionalInfo = binding.CardMoreInfo.editText?.text.toString()
                }
            )
        }

        /** Other Observations**/
        _viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                requireContext().toast(it)
                _viewModel.doneWithToastEvent()
            }
        })

        /** Form Validation Observations**/
        _viewModel.nameErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.CardName.error = it
        })

        _viewModel.numberErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.CardNumber.error = it
        })

        _viewModel.pinErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.CardPin.error = it
        })

        _viewModel.bankErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.CardBank.error = it
        })

        _viewModel.cardHolderErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.CardHolder.error = it
        })

        _viewModel.isFormValid.observe(viewLifecycleOwner, Observer {
            if (it) {
                //TODO: Add database code here to insert account
                requireContext().toast("Credentials has been stored.")
                requireView().findNavController()
                    .navigate(AddCardFragmentDirections.actionAddCardFragmentToFragmentCard())
            }
        })

        binding.IssuedDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                issuedDate,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH)
            ).show()
        }

        binding.ExpiryDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                expiryDate,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH)
            ).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateIssuedDateText(month: Int, year: Int) {
        binding.IssuedDate.setText("${month + 1}/$year")
    }

    private fun updateExpiryDateText(month: Int, year: Int) {
        binding.ExpiryDate.setText("${month + 1}/$year")
    }
}
