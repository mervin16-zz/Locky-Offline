package com.th3pl4gu3.locky.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.th3pl4gu3.locky.databinding.FragmentAccountBinding
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.ui.main.utils.toast

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        initiateAccountList().submitList(generateDummyAccounts())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initiateAccountList(): AccountAdapter{
        _binding!!.RecyclerViewAccount.layoutManager = GridLayoutManager(context, 3)

        val accountAdapter = AccountAdapter(AccountClickListener { account ->
            context!!.toast("Account ${account.name} was clicked. ID: ${account.id}")
        })

        _binding!!.RecyclerViewAccount.adapter = accountAdapter

        return accountAdapter
    }

    private fun generateDummyAccounts(): ArrayList<Account>{
        val account1 = Account("1")
        account1.name = "Facebook"
        account1.website = "facebook.com"

        val account2 = Account("2")
        account2.name = "Google"
        account2.website = "google.com"

        val account3 = Account("3")
        account3.name = "Youtube"
        account3.website = "youtube.com"

        val account4 = Account("4")
        account4.name = "Android"
        account4.website = "android.com"

        val account5 = Account("5")
        account5.name = "Apple"
        account5.website = "apple.com"

        val account6 = Account("6")
        account6.name = "Spotify"
        account6.website = "spotify.com"

        val account7 = Account("7")
        account7.name = "MCB"
        account7.website = "mcb.mu"

        val account8 = Account("8")
        account8.name = "Tinder"
        account8.website = "tinder.com"

        val account9 = Account("9")
        account9.name = "Udacity"
        account9.website = "udacity.com"

        val account10 = Account("10")
        account10.name = "udemy"
        account10.website = "udemy.com"

        val account11 = Account("11")
        account11.name = "Whatsapp"
        account11.website = "whatsappbrand.com"

        val account12 = Account("12")
        account12.name = "netflix"
        account12.website = "netflixinvestor.com"

        val accounts = java.util.ArrayList<Account>()
        accounts.add(account1)
        accounts.add(account2)
        accounts.add(account3)
        accounts.add(account4)
        accounts.add(account5)
        accounts.add(account6)
        accounts.add(account7)
        accounts.add(account8)
        accounts.add(account9)
        accounts.add(account10)
        accounts.add(account11)
        accounts.add(account12)

        return accounts
    }

}
