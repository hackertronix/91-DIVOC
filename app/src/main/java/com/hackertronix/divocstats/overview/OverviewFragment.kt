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
import com.hackertronix.divocstats.parseDate
import com.hackertronix.divocstats.toFlagEmoji
import kotlinx.android.synthetic.main.fragment_overview.appBar
import kotlinx.android.synthetic.main.fragment_overview.confirmed_textview
import kotlinx.android.synthetic.main.fragment_overview.country_flag
import kotlinx.android.synthetic.main.fragment_overview.deaths_textview
import kotlinx.android.synthetic.main.fragment_overview.locale_card
import kotlinx.android.synthetic.main.fragment_overview.recovered_textview
import kotlinx.android.synthetic.main.fragment_overview.updated_at
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
