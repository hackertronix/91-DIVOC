package com.hackertronix.divocstats.countrystats

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.YAxis.AxisDependency.LEFT
import com.github.mikephil.charting.data.LineData
import com.hackertronix.divocstats.R
import com.hackertronix.divocstats.common.UiState.Done
import com.hackertronix.divocstats.common.UiState.Loading
import com.hackertronix.divocstats.overview.OverviewFragment
import com.hackertronix.divocstats.overview.OverviewFragment.Companion
import com.hackertronix.divocstats.overview.OverviewFragment.Companion.ANIMATION_DURATION
import com.hackertronix.divocstats.parseDate
import com.hackertronix.divocstats.toFlagEmoji
import com.hackertronix.model.india.latest.LatestIndianStats
import kotlinx.android.synthetic.main.collapsing_card.updated_at
import kotlinx.android.synthetic.main.fragment_country_stats.appBar
import kotlinx.android.synthetic.main.fragment_country_stats.confirmed_textview
import kotlinx.android.synthetic.main.fragment_country_stats.content
import kotlinx.android.synthetic.main.fragment_country_stats.dead_chart
import kotlinx.android.synthetic.main.fragment_country_stats.deaths_textview
import kotlinx.android.synthetic.main.fragment_country_stats.swipeContainer
import kotlinx.android.synthetic.main.fragment_overview.confirmed_chart
import kotlinx.android.synthetic.main.shimmer_overview.shimmer_view
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class CountryStatsFragment : Fragment() {

    val viewModel: CountryStatsViewModel by viewModel()

    private var countryCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countryCode = it.getString(COUNTRY_CODE) ?: Locale.getDefault().country
            viewModel.setCountryCode(countryCode)
        }
    }

    private fun setupToolbar() {
        activity?.let {
            appBar.title = it.getString(R.string.country_stats, countryCode?.toFlagEmoji())
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

        subscribeToLatestStats()
        subscribeToRefreshState()
        subscribeToGraphingData()

        attachListeners()
    }

    private fun subscribeToGraphingData() {
        viewModel.getConfirmedDataset().observe(viewLifecycleOwner, Observer { dataSet ->

            dataSet.setDrawValues(false);
            dataSet.lineWidth = 2f;
            dataSet.setDrawCircles(false);

            val attrs = intArrayOf(android.R.attr.textColorSecondary)
            val typedArray = requireActivity().theme.obtainStyledAttributes(attrs)
            val textColor = typedArray.getColor(0, Color.WHITE)
            typedArray.recycle()

            dataSet.color = textColor

            val gradientDrawable = GradientDrawable(
                TOP_BOTTOM,
                intArrayOf(textColor, ColorUtils.setAlphaComponent(textColor, 0x4d))
            )
            dataSet.setDrawFilled(true)
            dataSet.fillDrawable = gradientDrawable
            confirmed_chart.visibility = View.VISIBLE
            confirmed_chart.apply {
                description.isEnabled = false
                setDrawBorders(false)
                setDrawGridBackground(false)
                setDrawBorders(false)
                setScaleEnabled(false)
                setPinchZoom(false)
                isDoubleTapToZoomEnabled = false
                setTouchEnabled(false)
                legend.isEnabled = false
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                xAxis.isEnabled = false

                setVisibleXRangeMaximum(30f)
                setVisibleXRangeMinimum(10000f)
                setVisibleYRangeMaximum(dataSet.yMax, LEFT)
                axisLeft.granularity = 50000f
                xAxis.granularity = 864000000f
                xAxis.isGranularityEnabled = true
            }
            confirmed_chart.data = LineData(dataSet)
            confirmed_chart.invalidate()

            confirmed_chart.animateX(OverviewFragment.ANIMATION_DURATION)
            confirmed_chart.animateY(OverviewFragment.ANIMATION_DURATION)
        })
        viewModel.getDeathsDataset().observe(viewLifecycleOwner, Observer { dataSet ->

            dataSet.setDrawValues(false);
            dataSet.lineWidth = 2f;
            dataSet.setDrawCircles(false);

            val attrs = intArrayOf(R.color.red)
            val typedArray = requireActivity().theme.obtainStyledAttributes(attrs)
            val textColor = typedArray.getColor(0, Color.RED)
            typedArray.recycle()

            dataSet.color = textColor

            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(textColor, ColorUtils.setAlphaComponent(textColor, 0x4d))
            )
            dataSet.setDrawFilled(true)
            dataSet.fillDrawable = gradientDrawable

            dead_chart.visibility = View.VISIBLE
            dead_chart.apply {
                description.isEnabled = false
                setDrawBorders(false)
                setDrawGridBackground(false)
                setDrawBorders(false)
                setScaleEnabled(false)
                setPinchZoom(false)
                isDoubleTapToZoomEnabled = false
                setTouchEnabled(false)
                legend.isEnabled = false
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                xAxis.isEnabled = false

                setVisibleXRangeMaximum(30f)
                setVisibleXRangeMinimum(10000f)
                setVisibleYRangeMaximum(dataSet.yMax, LEFT)
                axisLeft.granularity = 50000f
                xAxis.granularity = 864000000f
                xAxis.isGranularityEnabled = true
            }
            dead_chart.data = LineData(dataSet)
            dead_chart.invalidate()

            dead_chart.animateX(ANIMATION_DURATION)
            dead_chart.animateY(ANIMATION_DURATION)
        })
    }

    private fun attachListeners() {
        swipeContainer.setOnRefreshListener {
            viewModel.refreshLatestStats()
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

    private fun subscribeToLatestStats() {
        viewModel.getLatestStatsForCountry().observe(viewLifecycleOwner, Observer { stats ->
            confirmed_textview.text = String.format("%,d", stats.latest.confirmed)
            deaths_textview.text = String.format("%,d", stats.latest.deaths)
            updated_at.text = setDate(stats.lastUpdated.parseDate())
        })
    }

    private fun totalConfirmed(latestStat: LatestIndianStats): Int {
        return latestStat.data.summary.confirmedCasesForeign + latestStat.data.summary.confirmedCasesIndian
    }

    private fun setDate(parsedDate: String): String {
        val splitString = parsedDate.split(",")
        return resources.getString(R.string.updated_at_header, splitString.first(), splitString.last())
    }

    companion object {
        private const val COUNTRY_CODE = "country_code"
        const val INDIA = "IN"

        @JvmStatic
        fun newInstance(countryCode: String) =
            CountryStatsFragment().apply {
                arguments = Bundle().apply {
                    putString(COUNTRY_CODE, countryCode)
                }
            }
    }
}
