package com.hackertronix.divocstats.countrystats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackertronix.divocstats.R
import com.hackertronix.divocstats.parseDate
import com.hackertronix.divocstats.toFlagEmoji
import com.hackertronix.model.india.latest.Latest
import kotlinx.android.synthetic.main.collapsing_card.confirmed
import kotlinx.android.synthetic.main.collapsing_card.deaths
import kotlinx.android.synthetic.main.collapsing_card.recovered
import kotlinx.android.synthetic.main.collapsing_card.updated_at
import kotlinx.android.synthetic.main.fragment_country_stats.appBar
import kotlinx.android.synthetic.main.fragment_country_stats.collapsingToolbar
import kotlinx.android.synthetic.main.fragment_country_stats.recyclerView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountryStatsFragment : Fragment() {

    val viewModel: CountryStatsViewModel by viewModel()
    val adapter: CountryStatsAdapter by inject()

    private var countryCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countryCode = it.getString(COUNTRY_CODE)
        }
    }

    private fun setupToolbar() {
        activity?.let {
            collapsingToolbar.title = it.getString(R.string.country_stats, countryCode?.toFlagEmoji())
            (it as AppCompatActivity).setSupportActionBar(appBar)
            (it as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (it as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()

        viewModel.getLatestStats().observe(viewLifecycleOwner, Observer { latestStat ->
            setupHeader(latestStat)
            adapter.listOfRegions = latestStat.data.regional
            adapter.notifyDataSetChanged()
        })
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    private fun setupHeader(latestStat: Latest) {
        confirmed.text = resources.getString(R.string.confirmed_header, totalConfirmed(latestStat))
        recovered.text = resources.getString(R.string.recovered_header, latestStat.data.summary.discharged)
        deaths.text = resources.getString(R.string.deaths_header, latestStat.data.summary.deaths)
        updated_at.text = setDate(latestStat.lastRefreshed.parseDate())
    }

    private fun totalConfirmed(latestStat: Latest): Int {
        return latestStat.data.summary.confirmedCasesForeign + latestStat.data.summary.confirmedCasesIndian
    }

    private fun setDate(parsedDate: String): String {
        val splitString = parsedDate.split(",")
        return resources.getString(R.string.updated_at_header, splitString.first(), splitString.last())
    }

    companion object {
        private const val COUNTRY_CODE = "country_code"

        @JvmStatic
        fun newInstance(countryCode: String) =
            CountryStatsFragment().apply {
                arguments = Bundle().apply {
                    putString(COUNTRY_CODE, countryCode)
                }
            }
    }
}
