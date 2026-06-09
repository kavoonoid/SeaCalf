package com.teaml.seacalf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.teaml.seacalf.PreferencesManager

class FragmentStatsTab : Fragment() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_stats_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager.getInstance(requireContext())

        // load stats data
        val statsValues = preferencesManager.loadStats()
        for(i in 0..4) {
            val statItemId = resources.getIdentifier("stat_${i+1}", "id", requireContext().packageName)
            val statItem = view.findViewById<RelativeLayout>(statItemId)
            val statItemValue = statItem.getChildAt(2) as TextView
            statItemValue.text = statsValues[i].toString()
        }
    }
}