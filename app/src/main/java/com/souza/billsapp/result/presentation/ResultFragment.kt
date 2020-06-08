package com.souza.billsapp.result.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.souza.billsapp.R
import com.souza.billsapp.databinding.FragmentResultBinding
import com.souza.billsapp.extensions.formatValueToCoin
import com.souza.billsapp.extensions.visible

class ResultFragment : Fragment(){

    private lateinit var binding: FragmentResultBinding
    private val viewModel by viewModels<ResultViewModel>()
    private lateinit var nameTextView: TextView
    private lateinit var incomesTextView: TextView
    private lateinit var expensesTextView: TextView
    private lateinit var situationTextView: TextView
    private lateinit var chart: PieChart
    private val entries = mutableListOf<PieEntry>()
    private lateinit var dataSet: PieDataSet
    private var expensesResult: Float? = 0F
    private var incomesResult: Float? = 0F
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Acompanhamento"
        swipeRefreshLayout = binding.swipeRefreshLayoutResultFragment
        chart = binding.valuesPieChartResultFragment
        nameTextView = binding.textViewResumeResultFragment
        incomesTextView = binding.valueIncomesTextViewResultFragment
        expensesTextView = binding.valueExpensesTextViewResultFragment
        situationTextView = binding.valueSituationTextViewResultFragment

        setupRefreshLayout()
        initObserver()

        return binding.root
    }

    private fun setupRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener {
            entries.clear()
            expensesResult = 0F
            incomesResult  = 0F
            viewModel.getValuesOnRefresh()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initObserver() {
        viewModel.apply {
            this.updateSumResultOfExpenseOnLiveData().observe(viewLifecycleOwner, Observer {
                    expensesResult = it
                    expensesTextView.text = expensesResult?.let { formatValueToCoin(it) }
                    entries.add(PieEntry(it!!.toFloat(), "Despesas"))
                }
            )
            this.updateSumResultOfIncomeOnLiveData().observe(viewLifecycleOwner, Observer {
                    incomesResult = it
                    incomesTextView.text = incomesResult?.let { formatValueToCoin(it) }
                    entries.add(PieEntry(it!!.toFloat(), "Entradas"))
                    if(viewModel.updateSumResultOfExpenseOnLiveData().value != null) {
                        val situation = expensesResult?.let { it1 -> incomesResult?.minus(it1) }
                        situationTextView.text = situation?.let { formatValueToCoin(it) }
                        setupPieChart()
                        chart.visible()
                    }
                }
            )
        }
    }

    private fun setupPieChart(){
        chart.apply {
            description.isEnabled = false
            setExtraOffsets(10F ,0F ,5F ,5F )
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            holeRadius = 65f
            setDrawCenterText(true)
            setDrawEntryLabels(false)
            setUsePercentValues(true)
            rotationAngle = 0F
            isRotationEnabled = true
            setEntryLabelTextSize(8F)
            isHighlightPerTapEnabled = true
            animateY(1400, Easing.EaseInOutQuad)
        }
        setPieChartLegendAlignment()
        setPieChartDataSet()
    }

    private fun setPieChartLegendAlignment() {
        val chartLegend = chart.legend
        chartLegend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            orientation = Legend.LegendOrientation.VERTICAL
            setDrawInside(false)
            xEntrySpace = 7f
            textSize = 12F
            yEntrySpace = 0f
            yOffset = 0f
        }
    }

    private fun setPieChartDataSet(){
        dataSet = PieDataSet(entries, "")
        dataSet.valueFormatter = PercentFormatter(chart)
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        setPieChartDataSetColors()
        chart.data = data
    }

    private fun setPieChartDataSetColors(){
        val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(requireContext(), R.color.orangeLight))
        colors.add(ContextCompat.getColor(requireContext(), R.color.greeLight))
        dataSet.colors = colors
    }
}
