package com.hackertronix.divocstats.overview

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hackertronix.divocstats.MainActivity
import com.hackertronix.divocstats.R
import com.hackertronix.divocstats.common.UiState.Done
import com.hackertronix.divocstats.common.UiState.Loading
import com.hackertronix.divocstats.parseDate
import com.hackertronix.divocstats.toFlagEmoji
import kotlinx.android.synthetic.main.fragment_overview.appBar
import kotlinx.android.synthetic.main.fragment_overview.confirmed_textview
import kotlinx.android.synthetic.main.fragment_overview.content
import kotlinx.android.synthetic.main.fragment_overview.country_flag
import kotlinx.android.synthetic.main.fragment_overview.deaths_textview
import kotlinx.android.synthetic.main.fragment_overview.locale_card
import kotlinx.android.synthetic.main.fragment_overview.recovered_textview
import kotlinx.android.synthetic.main.fragment_overview.swipeContainer
import kotlinx.android.synthetic.main.fragment_overview.updated_at
import kotlinx.android.synthetic.main.shimmer_overview.shimmer_view
import org.koin.androidx.viewmodel.ext.android.viewModel

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()

        subscribeToOverviewData()
        subscribeToRefreshState()

        attachListeners()
    }

    private fun attachListeners() {
        locale_card.setOnClickListener {
            activity?.let {
                (it as MainActivity).showCountryStats(getCountryFromTelephonyManager())
            }
        }

        swipeContainer.setOnRefreshListener {
            viewModel.refreshOverview()
        }
    }

    private fun subscribeToRefreshState() {
        viewModel.getUiState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is Loading -> showLoading()
                is Error -> showContent()
                is Done -> showContent()
            }
        })
    }

    private fun showContent() {
        swipeContainer.isRefreshing = false
        content.visibility = View.VISIBLE
        shimmer_view.stopShimmer()
        shimmer_view.visibility = View.GONE
    }

    private fun showLoading() {
        swipeContainer.isRefreshing = true
        content.visibility = View.INVISIBLE
        shimmer_view.visibility = View.VISIBLE
        shimmer_view.startShimmer()
    }

    private fun setUpToolbar() {
        activity?.let {
            (it as AppCompatActivity).setSupportActionBar(appBar)
            (it as AppCompatActivity).supportActionBar?.title = it.getString(R.string.overview)
        }
    }

    private fun subscribeToOverviewData() {
        viewModel.getOverview().observe(viewLifecycleOwner, Observer { overview ->
            confirmed_textview.text = String.format("%,d", overview.confirmed.confirmedCasesCount)
            recovered_textview.text = String.format("%,d", overview.recovered.recoveredCasesCount)
            deaths_textview.text = String.format("%,d", overview.deaths.deathCasesCount)
            updated_at.text = setDate(overview.lastUpdate.parseDate())
            country_flag.text = getFlagFromLocale()
        })
    }

    private fun setDate(parsedDate: String): String {
        val splitString = parsedDate.split(",")
        return resources.getString(R.string.updated_at, splitString.first(), splitString.last())
    }

    private fun getFlagFromLocale(): String {

        return getCountryFromTelephonyManager().toFlagEmoji()
    }

    private fun getCountryFromTelephonyManager(): String {
        val telephonyManager = activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        return telephonyManager.simCountryIso
        return "IN"
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OverviewFragment().apply {
            }
    }
}
