package com.halogen.bit.ui.account

import android.graphics.Interpolator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.halogen.bit.MainActivity
import com.halogen.bit.R
import com.halogen.bit.model.DatabaseManager
import kotlinx.android.synthetic.main.fragment_profile.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProfileFragment : Fragment() {

    private val databaseManager: DatabaseManager by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStart() {
        super.onStart()

        //Setting the data for the chart
        val rawData = databaseManager.user.value!!.history
        val data = ArrayList<BarEntry>()

        for (i in 1..rawData.size) {
            data.add(BarEntry(
                    i.toFloat(),
                    rawData[i - 1].toFloat() / 3600f
            ))
        }

        val barDataSet = BarDataSet(data, "")
        barChart.data = BarData().apply{addDataSet(barDataSet)}

        //Bar Chart Settings
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setDrawGridBackground(false)
        barChart.isAutoScaleMinMaxEnabled = true

        //X Axis config
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Value Formatter
        barChart.xAxis.valueFormatter = object: ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return if (value.toInt() < 0 && value.toInt() > rawData.size - 1) { "" }
                else {
                    val days = value.toLong()
                    val date = LocalDate.now().minusDays(rawData.size - days)
                    date.format(DateTimeFormatter.ofPattern("d MMM"))
                }
            }
        }

        //Y Axis config
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 25f
        barChart.axisRight.axisMinimum = 0f
        barChart.axisRight.axisMaximum = 25f

        //Hiding Useless information
        barChart.description = Description()
        barChart.contentDescription = ""
        barChart.legend.isEnabled = false

        //Setting the ViewPort to show last 8 days
        barChart.setVisibleXRange(0f, 8f)
        barChart.setVisibleYRange(0f, 25f, YAxis.AxisDependency.LEFT)
        barChart.moveViewToAnimated((rawData.size - 7).toFloat(), 0f, YAxis.AxisDependency.LEFT, 1000)

        barChart.animateXY(500, 1000)
    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as MainActivity).toolbar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).drawer.openDrawer(GravityCompat.START)
        }
    }
}