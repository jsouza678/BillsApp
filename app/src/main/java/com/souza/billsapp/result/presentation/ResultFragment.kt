package com.souza.billsapp.result.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.souza.billsapp.R
import com.souza.billsapp.databinding.FragmentResultBinding
import com.souza.billsapp.result.utils.Constants.Companion.ANIMATION_DURATION_PIE_CHART
import com.souza.billsapp.result.utils.Constants.Companion.BOTTOM_PIE_CHART_EXTRA
import com.souza.billsapp.result.utils.Constants.Companion.DRAG_ACCELERATION_COEF_PIE_CHART
import com.souza.billsapp.result.utils.Constants.Companion.EMPTY_STRING
import com.souza.billsapp.result.utils.Constants.Companion.ENTRY_LABEL_TEXT_SIZE_PIE_CHART
import com.souza.billsapp.result.utils.Constants.Companion.FLOAT_ZERO_ABSOLUTE
import com.souza.billsapp.result.utils.Constants.Companion.HOLE_RADIUS_PIE_CHART
import com.souza.billsapp.result.utils.Constants.Companion.LEFT_PIE_CHART_EXTRA
import com.souza.billsapp.result.utils.Constants.Companion.LEGEND_TEXT_SIZE_PIE_CHART
import com.souza.billsapp.result.utils.Constants.Companion.RIGHT_PIE_CHART_EXTRA
import com.souza.billsapp.result.utils.Constants.Companion.TOP_PIE_CHART_EXTRA
import com.souza.billsapp.result.utils.Constants.Companion.X_ENTRY_SPACE_PIE_CHART
import com.souza.billsapp.result.utils.Constants.Companion.Y_ENTRY_SPACE_PIE_CHART
import com.souza.billsapp.sharedextensions.formatValueToCoin
import com.souza.billsapp.sharedextensions.visible

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val viewModel by viewModels<ResultViewModel>()
    private lateinit var nameTextView: TextView
    private lateinit var incomesTextView: TextView
    private lateinit var expensesTextView: TextView
    private lateinit var situationTextView: TextView
    private lateinit var chart: PieChart
    private val entries = mutableListOf<PieEntry>()
    private lateinit var dataSet: PieDataSet
    private var expensesResult: Float? = FLOAT_ZERO_ABSOLUTE
    private var incomesResult: Float? = FLOAT_ZERO_ABSOLUTE
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        bottomNavigationView = activity?.findViewById(R.id.bottom_nav_menu)!!

        setupRefreshLayout()
        initObserver()

        return binding.root
    }

    private fun setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            entries.clear()
            expensesResult = FLOAT_ZERO_ABSOLUTE
            incomesResult = FLOAT_ZERO_ABSOLUTE
            viewModel.getValuesOnRefresh()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initObserver() {
        viewModel.apply {
            this.updateSumResultOfExpenseOnLiveData().observe(viewLifecycleOwner, Observer { expenseSumResult ->
                expensesResult = expenseSumResult
                expensesTextView.text = expensesResult?.let { formatValueToCoin(it) }
                expenseSumResult?.let { PieEntry(it, "Despesas") }?.let { entries.add(it) }
            })
            this.updateSumResultOfIncomeOnLiveData().observe(viewLifecycleOwner, Observer { incomeSumResult ->
                incomesResult = incomeSumResult
                incomesTextView.text = incomesResult?.let { formatValueToCoin(it) }
                incomeSumResult?.let { PieEntry(it, "Entradas") }?.let { entries.add(it) }
                if (viewModel.updateSumResultOfExpenseOnLiveData().value != null) {
                    val situation = expensesResult?.let { it1 -> incomesResult?.minus(it1) }
                    situationTextView.text = situation?.let { formatValueToCoin(it) }
                    if (situation != null) {
                        if (situation > 0.0) {
                            situationTextView.setTextColor(ContextCompat.getColor(requireContext(),
                                R.color.greeLight))
                        } else {
                            situationTextView.setTextColor(ContextCompat.getColor(requireContext(),
                                R.color.colorPrimary))
                        }
                    }
                    setupPieChart()
                    chart.visible()
                }
            })
        }
    }

    private fun setupPieChart() {
        chart.apply {
            description.isEnabled = false
            setExtraOffsets(LEFT_PIE_CHART_EXTRA, TOP_PIE_CHART_EXTRA, RIGHT_PIE_CHART_EXTRA, BOTTOM_PIE_CHART_EXTRA)
            dragDecelerationFrictionCoef = DRAG_ACCELERATION_COEF_PIE_CHART
            isDrawHoleEnabled = true
            holeRadius = HOLE_RADIUS_PIE_CHART
            setDrawCenterText(true)
            setDrawEntryLabels(false)
            setUsePercentValues(true)
            rotationAngle = FLOAT_ZERO_ABSOLUTE
            isRotationEnabled = true
            setEntryLabelTextSize(ENTRY_LABEL_TEXT_SIZE_PIE_CHART)
            isHighlightPerTapEnabled = true
            animateY(ANIMATION_DURATION_PIE_CHART, Easing.EaseInOutQuad)
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
            xEntrySpace = X_ENTRY_SPACE_PIE_CHART
            textSize = LEGEND_TEXT_SIZE_PIE_CHART
            yEntrySpace = Y_ENTRY_SPACE_PIE_CHART
            yOffset = FLOAT_ZERO_ABSOLUTE
        }
    }

    private fun setPieChartDataSet() {
        dataSet = PieDataSet(entries, EMPTY_STRING)
        dataSet.valueFormatter = PercentFormatter(chart)
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        setPieChartDataSetColors()
        chart.data = data
    }

    private fun setPieChartDataSetColors() {
        val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(requireContext(), R.color.orangeLight))
        colors.add(ContextCompat.getColor(requireContext(), R.color.greeLight))
        dataSet.colors = colors
    }

    override fun onResume() {
        super.onResume()
        turnOnBottomNavigation()
    }

    private fun turnOnBottomNavigation() {
        bottomNavigationView.visible()
    }
}
