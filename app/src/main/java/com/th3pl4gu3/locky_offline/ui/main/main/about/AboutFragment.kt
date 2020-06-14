package com.th3pl4gu3.locky_offline.ui.main.main.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentAboutBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.openMail
import com.th3pl4gu3.locky_offline.ui.main.utils.openUrl
import com.th3pl4gu3.locky_offline.ui.main.utils.share

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private var _viewModel: AboutViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)

        // Bind lifecycle owner
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Recyclerview for Development Part */
        loadDevelopmentList()

        /* Recyclerview fro Other part */
        loadOtherList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun developmentRedirection(item: AboutItem.Item) {
        when (item) {
            AboutItem.Item.DEV_RATE_US -> ratingRedirection()

            AboutItem.Item.DEV_DONATE -> donationRedirection()

            AboutItem.Item.DEV_SHARE -> shareRedirection()

            AboutItem.Item.DEV_REPORT_BUG -> bugReportRedirection()

            else -> return
        }
    }

    private fun otherRedirection(item: AboutItem.Item) {
        when (item) {

            AboutItem.Item.OTHER_LICENSES -> licensesRedirection()

            AboutItem.Item.OTHER_DEVELOPER -> developerRedirection()

            AboutItem.Item.OTHER_POLICY -> policyRedirection()

            else -> return
        }
    }

    private fun ratingRedirection() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                getString(R.string.app_play_link, requireContext().applicationContext.packageName)
            )
            setPackage(getString(R.string.app_play_package))
        }
        startActivity(intent)
    }


    private fun donationRedirection() {
        navigateTo(AboutFragmentDirections.actionFragmentAboutToFragmentDonate())
    }

    private fun developerRedirection() {
        val intent = openUrl(getString(R.string.app_url_developer_api))
        if (isIntentSafeToStart(intent)) startActivity(intent) else showDialog("Browser")
    }

    private fun policyRedirection() {
        val intent = openUrl(getString(R.string.app_url_privacy_policy))
        if (isIntentSafeToStart(intent)) startActivity(intent) else showDialog("Browser")
    }

    private fun licensesRedirection() {
        startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
    }

    private fun shareRedirection() {
        val sendIntent: Intent = share(
            getString(
                R.string.app_play_share,
                requireContext().applicationContext.packageName
            )
        )
        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun bugReportRedirection() {
        val intent = openMail(
            arrayOf(getString(R.string.app_support_email_team)),
            getString(R.string.app_support_email_subject_bug)
        )

        if (isIntentSafeToStart(intent)) startActivity(intent) else showDialog("email")
    }

    private fun isIntentSafeToStart(intent: Intent) =
        intent.resolveActivity(requireActivity().packageManager) != null

    private fun showDialog(app: String) =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_alert_intent_none, app))
            .setMessage(getString(R.string.text_message_alert_intent_none, "an email"))
            .setPositiveButton(R.string.button_action_okay) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    private fun loadDevelopmentList() {
        val developmentAdapter = AboutItemAdapter(
            ItemClickListener {
                developmentRedirection(it)
            })

        binding.includeAboutDevelopment.RecyclerViewDevelopment.apply {
            adapter = developmentAdapter
            setHasFixedSize(true)
        }

        developmentAdapter.submitList(viewModel.getDevelopmentList())

    }

    private fun loadOtherList() {
        val developmentAdapter = AboutItemAdapter(
            ItemClickListener {
                otherRedirection(it)
            })

        binding.includeAboutOther.RecyclerViewOther.apply {
            adapter = developmentAdapter
            setHasFixedSize(true)
        }

        developmentAdapter.submitList(viewModel.getOtherList())

    }
}
