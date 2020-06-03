package com.souza.billsapp.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp

import com.souza.billsapp.R
import com.souza.billsapp.data.Expense
import com.souza.billsapp.databinding.FragmentInsertExpenseBinding
import java.nio.channels.CancelledKeyException
import java.util.*

class InsertExpenseFragment : Fragment() {

    private lateinit var navController : NavController
    private lateinit var binding : FragmentInsertExpenseBinding
    private val viewModel by viewModels<ExpenseViewModel>()
    private lateinit var valueInputEditText: TextInputEditText
    private lateinit var descriptionInputEditText : TextInputEditText
    private lateinit var wasPaidCheckBox : CheckBox
    private lateinit var insertExpenseButton: Button
    private lateinit var openDatePickerButton: Button
    private val calendar = Calendar.getInstance()
    private lateinit var dateSetListener : DatePickerDialog.OnDateSetListener
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var date : Date = calendar.time

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentInsertExpenseBinding>(inflater, R.layout.fragment_insert_expense, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        insertExpenseButton = binding.insertExpenseButton
        valueInputEditText = binding.valueTextInputEditTextExpenseFragment
        descriptionInputEditText = binding.descriptionTextInputEditTextExpenseFragment
        wasPaidCheckBox = binding.wasPaidCheckboxExpenseFragment
        openDatePickerButton = binding.datePickerButtonExpenseFragment

        setupDatePickerDialogListener()

        insertExpenseButton.setOnClickListener{
            if(!valueInputEditText.text.isNullOrBlank()){
                val valueResult : Int = Integer.parseInt(valueInputEditText.text.toString())
                val descriptionResult : String = descriptionInputEditText.text.toString()
                val paidResult = wasPaidCheckBox.isChecked
                val dateResult = Timestamp(date)

                val data = Expense(
                    valueResult,
                    descriptionResult,
                    dateResult,
                    paidResult
                )
                viewModel.insertExpense(data)
                navController.navigate(R.id.action_insertExpenseFragment_to_billFragment)
            }else {
                valueInputEditText.error = "ERROR"
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
                date = Date(dayOfMonth, monthOfYear, year)
            }, year, month, day).show()
        }
    }
}
