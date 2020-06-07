package com.souza.billsapp.expense.presentation

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.souza.billsapp.data.Expense
import com.souza.billsapp.databinding.FragmentInsertExpenseBinding
import com.souza.billsapp.extensions.invisible
import com.souza.billsapp.extensions.visible
import com.squareup.picasso.Picasso
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class InsertExpenseFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentInsertExpenseBinding
    private val viewModel by viewModels<ExpenseViewModel>()
    private lateinit var valueInputEditText: TextInputEditText
    private lateinit var descriptionInputEditText: TextInputEditText
    private lateinit var wasPaidCheckBox: CheckBox
    private lateinit var insertExpenseButton: Button
    private lateinit var insertedImage: ImageView
    private lateinit var openDatePickerButton: Button
    private lateinit var insertImageButton: ImageButton
    private lateinit var progressBarImageUpdate: ProgressBar
    private lateinit var dateSelectedOnDatePickerTextView: TextView
    private val calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var date: Date = calendar.time
    private lateinit var choosenDate: Date
    private var documentId = ""
    private var isUpdate = false
    private lateinit var safeArgs: InsertExpenseFragmentArgs
    private lateinit var imageUri: Uri
    private var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentInsertExpenseBinding>(
            inflater,
            R.layout.fragment_insert_expense,
            container,
            false
        )
        (activity as AppCompatActivity).supportActionBar?.title = ""
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        (activity as AppCompatActivity).supportActionBar?.show()

        progressBarImageUpdate = binding.progressBarImageProgressUploadExpenseFragment
        insertedImage = binding.imageViewAttachExpenseFragment
        insertExpenseButton = binding.insertExpenseButton
        insertImageButton = binding.imageAttachButtonInsertExpenseFragment
        valueInputEditText = binding.valueTextInputEditTextExpenseFragment
        descriptionInputEditText = binding.descriptionTextInputEditTextExpenseFragment
        wasPaidCheckBox = binding.wasPaidCheckboxExpenseFragment
        openDatePickerButton = binding.datePickerButtonExpenseFragment
        dateSelectedOnDatePickerTextView = binding.dateSelectedTextViewExpenseFragment

        arguments?.let {
            safeArgs = InsertExpenseFragmentArgs.fromBundle(it)
            documentId = safeArgs.documentId
            if (safeArgs.documentId != "-1") {
                isUpdate = true
            }
        }

        if (isUpdate) {
            (activity as AppCompatActivity).supportActionBar?.title = "Editar gasto"
            setupUpdateExpense(safeArgs)
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = "Inserir gasto"
        }

        setupDatePickerDialogListener()

        choosenDate = calendar.time
        insertImageButton.setOnClickListener {
            openFileChooser()
        }

        setupInsertExpenseButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        )
                || super.onOptionsItemSelected(item)
    }

    private fun setupInsertExpenseButton() {
        insertExpenseButton.setOnClickListener {
            val valueResult = valueInputEditText.text.toString()
            val descriptionResult: String = descriptionInputEditText.text.toString()
            val paidResult = wasPaidCheckBox.isChecked
            val dateResult = Timestamp(choosenDate)

            if (valueResult.trim().isEmpty() || descriptionResult.trim().isEmpty()) {
                valueInputEditText.error = "Por favor, preencha o valor"
                descriptionInputEditText.error = "Por favor, preencha a descrição"
            } else {
                val data = Expense(
                    valueResult.toFloat(),
                    descriptionResult,
                    dateResult,
                    paidResult,
                    imageUrl.toString()
                )
                if (isUpdate) {
                    viewModel.updateExpense(data, documentId)
                } else {
                    viewModel.insertExpense(data)
                }
                navController.navigate(R.id.action_insertExpenseFragment_to_billFragment)
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            insertExpenseButton.invisible()
            progressBarImageUpdate.visible()
            Picasso.get().load(imageUri).into(insertedImage)
            viewModel.insertExpenseImageAttach(imageUri)
            initAttachObserver()
        }
    }

    private fun initAttachObserver () {
        viewModel.apply {
            this.updateExpenseImageURLOnLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it != null) {
                    imageUrl = it
                    progressBarImageUpdate.invisible()
                    insertExpenseButton.visible()
                }
            })
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
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    choosenDate = calendar.time
                    dateSelectedOnDatePickerTextView.text = formatDate(choosenDate)
                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun setupUpdateExpense(safeArgs: InsertExpenseFragmentArgs) {
        valueInputEditText.text = safeArgs.value.toString().toEditable()
        descriptionInputEditText.text = safeArgs.description.toString().toEditable()
        wasPaidCheckBox.isChecked = safeArgs.wasPaid
        val date = safeArgs.date?.toDate()
        dateSelectedOnDatePickerTextView.text = formatDateWithSeconds(date)
    }

    private fun formatDateWithSeconds(date: Date?): String {
        val formatter: Format = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }

    private fun formatDate(date: Date?): String {
        val formatter: Format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}
