package com.souza.billsapp.income.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.souza.billsapp.R
import com.souza.billsapp.data.Income
import com.souza.billsapp.databinding.FragmentInsertIncomesBinding
import com.souza.billsapp.result.presentation.ResultViewModel
import java.text.Format
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InsertIncomeFragment : Fragment() {

    private lateinit var navController : NavController
    private lateinit var binding : FragmentInsertIncomesBinding
    private val viewModel by viewModels<IncomeViewModel>()
    private lateinit var valueInputEditText: TextInputEditText
    private lateinit var descriptionInputEditText : TextInputEditText
    private lateinit var wasPaidCheckBox : CheckBox
    private lateinit var insertIncomeButton: Button
    private lateinit var openDatePickerButton: Button
    private lateinit var dateSelectedOnDatePickerTextView: TextView
    private val calendar = Calendar.getInstance()
    private lateinit var dateSetListener : DatePickerDialog.OnDateSetListener
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var date : Date = calendar.time
    private lateinit var choosenDate : Date
    private var documentId = ""
    private var isUpdate = false
    private lateinit var safeArgs: InsertIncomeFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentInsertIncomesBinding>(inflater, R.layout.fragment_insert_incomes, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        /*NavigationUI.setupActionBarWithNavController(activity as AppCompatActivity, navController)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)*/
        (activity as AppCompatActivity).supportActionBar?.show()

        insertIncomeButton = binding.insertIncomeButton
        valueInputEditText = binding.valueTextInputEditTextIncomeFragment
        descriptionInputEditText = binding.descriptionTextInputEditTextIncomeFragment
        wasPaidCheckBox = binding.wasPaidCheckboxIncomeFragment
        openDatePickerButton = binding.datePickerButtonIncomeFragment
        dateSelectedOnDatePickerTextView = binding.dateSelectedTextViewIncomeFragment

        arguments?.let {
            safeArgs = InsertIncomeFragmentArgs.fromBundle(it)
            documentId = safeArgs.documentId
            if(safeArgs.documentId != "-1"){ isUpdate = true }
        }

        if(isUpdate){
            (activity as AppCompatActivity).supportActionBar?.title = "Editar entrada"
            setupUpdateIncome(safeArgs)
        }else {
            (activity as AppCompatActivity).supportActionBar?.title = "Inserir entrada"
        }

        setupDatePickerDialogListener()
        choosenDate = calendar.time
        setupInsertIncomeButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle the up button here
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun setupInsertIncomeButton() {
        insertIncomeButton.setOnClickListener{
            val valueResult = valueInputEditText.text.toString()
            val descriptionResult : String = descriptionInputEditText.text.toString()
            val paidResult = wasPaidCheckBox.isChecked
            val dateResult = Timestamp(choosenDate)

            if(valueResult.trim().isEmpty() || descriptionResult.trim().isEmpty()){
                valueInputEditText.error = "Por favor, preencha o valor"
                descriptionInputEditText.error = "Por favor, preencha a descrição"
            } else {
                val data = Income(
                    Integer.parseInt(valueResult),
                    descriptionResult,
                    dateResult,
                    paidResult
                )
                if(isUpdate){
                    viewModel.updateIncome(data, documentId)
                }else{
                    viewModel.insertIncome(data)
                }
                navController.navigate(R.id.action_insertIncomeFragment_to_incomeFragment)
            }
        }
    }

    private fun setupDatePickerDialogListener() {
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        setupDatePickerButtonListener()
    }

    private fun setupDatePickerButtonListener() {
        openDatePickerButton.setOnClickListener {
            DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                choosenDate = calendar.time
                dateSelectedOnDatePickerTextView.text = formatDate(choosenDate)
            }, year, month, day).show()
        }
    }

    private fun setupUpdateIncome(safeArgs: InsertIncomeFragmentArgs ) {
        valueInputEditText.text = safeArgs.value.toString().toEditable()
        descriptionInputEditText.text = safeArgs.description.toString().toEditable()
        wasPaidCheckBox.isChecked = safeArgs.wasReceived
        val date = safeArgs.date?.toDate()
        dateSelectedOnDatePickerTextView.text = formatDateWithSeconds(date)
    }

    private fun formatDateWithSeconds(date : Date?) : String{
        val formatter: Format = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }

    private fun formatDate(date : Date?) : String{
        val formatter: Format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}
