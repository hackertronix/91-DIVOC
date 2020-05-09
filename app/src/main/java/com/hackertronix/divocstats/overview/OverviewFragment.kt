package com.hackertronix.divocstats.overview

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.hackertronix.divocstats.*
import com.hackertronix.divocstats.common.UiState.Done
import com.hackertronix.divocstats.common.UiState.Loading
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.shimmer_overview.*
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

        setupCharts()
        subscribeToOverviewData()
        subscribeToGraphingData()
        subscribeToRefreshState()

        attachListeners()
    }

    private fun subscribeToGraphingData() {
        viewModel.getConfirmedDataSet().observe(viewLifecycleOwner, Observer { dataSet ->

            dataSet.setDrawValues(false);
            dataSet.lineWidth = 2f;
            dataSet.setDrawCircles(false);

            val attrs = intArrayOf(android.R.attr.textColorSecondary)
            val typedArray = requireActivity().theme.obtainStyledAttributes(attrs)
            val textColor = typedArray.getColor(0, Color.WHITE)
            typedArray.recycle()

            dataSet.color = textColor

            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(textColor, ColorUtils.setAlphaComponent(textColor, 0x4d))
            )
            dataSet.setDrawFilled(true)
            dataSet.fillDrawable = gradientDrawable
            confirmed_chart.apply {
                visibility = View.VISIBLE
                data = LineData(dataSet)
                animateXY(ANIMATION_DURATION, ANIMATION_DURATION)
            }
        })

        viewModel.getDeathsDataSet().observe(viewLifecycleOwner, Observer { dataSet ->

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

            deaths_chart.apply {
                visibility = View.VISIBLE
                data = LineData(dataSet)
                animateXY(ANIMATION_DURATION, ANIMATION_DURATION)
            }
        })

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

    private fun setupCharts() {
        setupChart(confirmed_chart)
        setupChart(deaths_chart)
    }

    private fun setupChart(lineChart: LineChart) {
        lineChart.apply {
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
            axisLeft.granularity = 50000f
            xAxis.granularity = 864000000f
            xAxis.isGranularityEnabled = true

            setVisibleXRangeMaximum(30f)
            setVisibleXRangeMinimum(10000f)
        }
    }

    private fun subscribeToOverviewData() {
        viewModel.getOverview().observe(viewLifecycleOwner, Observer { overview ->
            confirmed_textview.text = String.format("%,d", overview.confirmed.confirmedCasesCount)
            deaths_textview.text = String.format("%,d", overview.deaths.deathCasesCount)
            updated_at.text = setDate(overview.lastUpdate.parseDate())
            country_stat_label.text = getCountryNameFromSim()
            country_flag.text = getFlagFromLocale()
        })
    }

    private fun getCountryNameFromSim() = getString(
        R.string.see_your_country_s_stats,
        getCountryFromTelephonyManager().toFullCountryName()
    )


    private fun setDate(parsedDate: String): String {
        val splitString = parsedDate.split(",")
        return resources.getString(R.string.updated_at, splitString.first(), splitString.last())
    }

    private fun getFlagFromLocale() = getCountryFromTelephonyManager().toFlagEmoji()


    private fun getCountryFromTelephonyManager(): String {
       /* val telephonyManager =
            activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.simCountryIso.run {
            if (this.isEmpty()) {
                return@run Locale.getDefault().country
            } else return@run this
        }.toUpperCase()*/
        return "GB"
    }

    companion object {

        const val ANIMATION_DURATION = 1000

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OverviewFragment().apply {
            }
    }
}
