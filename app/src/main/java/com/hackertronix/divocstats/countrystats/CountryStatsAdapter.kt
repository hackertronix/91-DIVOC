package com.hackertronix.divocstats.countrystats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hackertronix.divocstats.R
import com.hackertronix.divocstats.countrystats.CountryStatsAdapter.CountryStatsViewHolder
import com.hackertronix.model.india.latest.Regional

class CountryStatsAdapter : RecyclerView.Adapter<CountryStatsViewHolder>() {

    var listOfRegions: List<Regional> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryStatsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.country_stat_item,parent,false)
        return CountryStatsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listOfRegions.size
    }

    override fun onBindViewHolder(holder: CountryStatsViewHolder, position: Int) {
        val region = listOfRegions[position]

        holder.bind(region)
    }

    inner class CountryStatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(region: Regional) {
            location.text = region.loc
            confirmed.text = itemView.context.resources.getString(R.string.confirmed_header,parseConfirmed(region))
            recovered.text = itemView.context.resources.getString(R.string.recovered_header,region.discharged)
            deaths.text = itemView.context.resources.getString(R.string.deaths_header,region.deaths)
        }

        private fun parseConfirmed(region: Regional): Int {
            return region.confirmedCasesForeign+region.confirmedCasesIndian
        }

        val location: TextView
        val confirmed: TextView
        val recovered: TextView
        val deaths: TextView
        init {
            location = itemView.findViewById(R.id.location) as TextView
            confirmed = itemView.findViewById(R.id.confirmed) as TextView
            recovered = itemView.findViewById(R.id.recovered) as TextView
            deaths = itemView.findViewById(R.id.deaths) as TextView
        }
    }

}