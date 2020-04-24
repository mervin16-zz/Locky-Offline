package com.th3pl4gu3.locky.ui.main.add.card

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.FragmentAddCardBinding
import com.th3pl4gu3.locky.ui.main.utils.setCardLogo
import com.th3pl4gu3.locky.ui.main.utils.toFormattedCalendar
import com.th3pl4gu3.locky.ui.main.utils.toFormattedString
import com.th3pl4gu3.locky.ui.main.utils.toast
import java.util.*


class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private lateinit var _viewModel: AddCardViewModel
    private lateinit var _card: Card

    private val binding get() = _binding!!

    private var _issuedDate: OnDateSetListener = OnDateSetListener { _, year, month, _ ->
        updateIssuedDateText(month, year)
    }

    private var _expiryDate: OnDateSetListener = OnDateSetListener { _, year, month, _ ->
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
        _card = AddCardFragmentArgs.fromBundle(requireArguments()).parcelcredcard ?: Card()

        with(_viewModel) {

            setCard(_card)

            /** Other Observations**/
            toastEvent.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    toast(it)
                    doneWithToastEvent()
                }
            })

            /** Form Validation Observations**/
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

            isFormValid.observe(viewLifecycleOwner, Observer {
                if (it) {
                    toast(getString(R.string.message_credentials_created, _card.name))
                    findNavController().navigate(AddCardFragmentDirections.actionFragmentAddCardToFragmentCard())
                }
            })
        }

        with(binding) {

            ButtonSave.setOnClickListener {
                _viewModel.isFormValid(
                    _card.apply {
                        name = binding.CardName.editText?.text.toString()
                        number = binding.CardNumber.editText?.text.toString().toLong()
                        pin = binding.CardPin.editText?.text.toString().toInt()
                        bank = binding.CardBank.editText?.text.toString()
                        cardHolderName = binding.CardHolder.editText?.text.toString()
                        issuedDate =
                            binding.CardIssuedDate.editText?.text.toString().toFormattedCalendar().toFormattedString()
                        expiryDate =
                            binding.CardIssuedDate.editText?.text.toString().toFormattedCalendar().toFormattedString()
                        additionalInfo = binding.CardMoreInfo.editText?.text.toString()
                    }
                )
            }

            IssuedDate.setOnClickListener {
                val cal = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(),
                    _issuedDate,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH)
                ).show()
            }

            ExpiryDate.setOnClickListener {
                val cal = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(),
                    _expiryDate,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH)
                ).show()
            }

            CardNumber.editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count > 0) {
                        binding.CardLogo.setCardLogo(s.toString().toLong())
                    }
                }

            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateIssuedDateText(month: Int, year: Int) = binding.IssuedDate.setText(
        getString(
            R.string.field_card_date_formatter,
            (month + 1).toString(),
            year.toString()
        )
    )

    private fun updateExpiryDateText(month: Int, year: Int) = binding.ExpiryDate.setText(
        getString(
            R.string.field_card_date_formatter,
            (month + 1).toString(),
            year.toString()
        )
    )

    private fun toast(message: String) = requireContext().toast(message)
}
