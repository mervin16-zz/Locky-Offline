package com.th3pl4gu3.locky.ui.main.main.search

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.core.main.Card
import com.th3pl4gu3.locky.databinding.FragmentBottomSheetSearchBinding
import com.th3pl4gu3.locky.ui.main.main.account.AccountAdapter
import com.th3pl4gu3.locky.ui.main.main.account.AccountClickListener
import com.th3pl4gu3.locky.ui.main.main.account.AccountOptionsClickListener
import com.th3pl4gu3.locky.ui.main.main.card.CardAdapter
import com.th3pl4gu3.locky.ui.main.main.card.CardClickListener
import com.th3pl4gu3.locky.ui.main.main.card.CardOptionsClickListener
import com.th3pl4gu3.locky.ui.main.utils.*


class SearchBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetSearchBinding? = null
    private var _viewModel: SearchViewModel? = null
    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<View>

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetSearchBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSnackBarEvent()

        setUpNestedScrollChangeListener()

        observeCardListEvent()

        observeAccountListEvent()

        listenerForButtonCancel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeSnackBarEvent() {
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                snackBarAction(it)
            }
        })
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        bottomSheetDialogSetup(bottomSheetDialog)
    }

    private fun bottomSheetDialogSetup(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet as View)
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        val layoutParams = bottomSheet.layoutParams

        if (layoutParams != null) {
            layoutParams.height = getWindowHeight()
        }

        bottomSheet.layoutParams = layoutParams
        bottomSheetBehaviour.peekHeight = getWindowHeight()
    }

    private fun observeAccountListEvent() {
        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //Set the loading status to DONE
                viewModel.doneLoadingAccounts()

                initiateAccountList().submitList(it)
            }
        })
    }

    private fun observeCardListEvent() {
        viewModel.cards.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //Set the loading status to DONE
                viewModel.doneLoadingCards()

                initiateCardList().submitList(it)
            }
        })
    }

    private fun listenerForButtonCancel() {
        binding.ButtonCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initiateCardList(): CardAdapter {
        val cardAdapter = CardAdapter(
            CardClickListener {

            },
            CardOptionsClickListener { view, card ->
                createCardPopupMenu(view, card)
            })

        binding.RecyclerViewCards.apply {
            adapter = cardAdapter
            setHasFixedSize(true)
        }

        return cardAdapter
    }

    private fun initiateAccountList(): AccountAdapter {
        val accountAdapter =
            AccountAdapter(
                AccountClickListener {

                },
                AccountOptionsClickListener { view, account ->
                    createActionPopupMenu(view, account)
                })

        binding.RecyclerViewAccounts.apply {
            adapter = accountAdapter
            setHasFixedSize(true)
        }

        return accountAdapter
    }

    private fun setUpNestedScrollChangeListener() =
        with(binding) {
            LayoutNestedScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                if (scrollY > 0) {
                    LayoutFixedTopView.elevation = 8F
                } else {
                    LayoutFixedTopView.elevation = 0F
                }
            }
        }

    private fun createCardPopupMenu(view: View, card: Card) {
        requireContext().createPopUpMenu(
            view,
            R.menu.menu_moreoptions_card,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyNumber -> copyToClipboardAndToast(card.number.toCreditCardFormat())
                    R.id.Menu_CopyPin -> copyToClipboardAndToast(card.pin)
                    R.id.Menu_ShowPin -> triggerSnackBarAction(card.pin)
                    else -> false
                }
            }, PopupMenu.OnDismissListener {
                view.apply {
                    isEnabled = true
                }
            })
    }

    private fun createActionPopupMenu(view: View, account: Account) {
        requireContext().createPopUpMenu(
            view,
            R.menu.menu_moreoptions_account,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyUsername -> copyToClipboardAndToast(account.username)
                    R.id.Menu_CopyPass -> copyToClipboardAndToast(account.password)
                    R.id.Menu_ShowPass -> triggerSnackBarAction(account.password)
                    else -> false
                }
            }, PopupMenu.OnDismissListener {
                view.apply {
                    isEnabled = true
                }
            })
    }

    private fun snackBarAction(message: String) {
        binding.LayoutNestedScroll.snackbar(message) {
            action(getString(R.string.button_snack_action_close)) { dismiss() }
        }
        viewModel.doneShowingSnackBar()
    }

    private fun triggerSnackBarAction(message: String): Boolean {
        viewModel.setSnackBarMessage(message)
        return true
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun toast(message: String) = requireContext().toast(message)

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay
            .getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}