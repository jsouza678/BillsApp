package com.souza.billsapp.expensecatalog.presentation

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
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.souza.billsapp.R
import com.souza.billsapp.databinding.FragmentInsertExpenseBinding
import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.expensecatalog.utils.Constants.Companion.EMPTY_STRING
import com.souza.billsapp.expensecatalog.utils.Constants.Companion.STARTING_VALUE_DOCUMENT_ID
import com.souza.billsapp.sharedextensions.formatDate
import com.souza.billsapp.sharedextensions.formatDateWithSeconds
import com.souza.billsapp.sharedextensions.invisible
import com.souza.billsapp.sharedextensions.isValidUrl
import com.souza.billsapp.sharedextensions.loadImageUrl
import com.souza.billsapp.sharedextensions.visible
import java.util.Calendar
import java.util.Date
import org.koin.android.viewmodel.ext.android.viewModel

class InsertExpenseFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentInsertExpenseBinding
    private val viewModel by viewModel<ExpenseCatalogViewModel>()
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
    private lateinit var choosenDate: Date
    private var documentId = EMPTY_STRING
    private var isUpdate = false
    private lateinit var safeArgs: InsertExpenseFragmentArgs
    private lateinit var imageUri: Uri
    private var imageUrl = EMPTY_STRING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentInsertExpenseBinding>(
            inflater,
            R.layout.fragment_insert_expense,
            container,
            false
        )
        (activity as AppCompatActivity).supportActionBar?.title = EMPTY_STRING
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
            if (safeArgs.documentId != STARTING_VALUE_DOCUMENT_ID) {
                isUpdate = true
            }
        }

        if (isUpdate) {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.edit_expense_fragment_title)
            setupUpdateExpense(safeArgs)
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.inser_expense_fragment_title)
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
        ) ||
                super.onOptionsItemSelected(item)
    }

    private fun setupInsertExpenseButton() {
        insertExpenseButton.setOnClickListener {
            val valueResult = valueInputEditText.text.toString()
            val descriptionResult: String = descriptionInputEditText.text.toString()
            val paidResult = wasPaidCheckBox.isChecked
            val dateResult = Timestamp(choosenDate)

            if (valueResult.trim().isEmpty() || descriptionResult.trim().isEmpty()) {
                valueInputEditText.error = getString(R.string.error_message_input_layout_value_insert_fragment)
                descriptionInputEditText.error = getString(R.string.error_message_input_layout_description_insert_fragment)
            } else {
                val data = Expense(
                    valueResult.toFloat(),
                    descriptionResult,
                    dateResult,
                    paidResult,
                    imageUrl
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
        intent.type = getString(R.string.file_chooser_image_path_intent)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            insertedImage.loadImageUrl(imageUri.toString())
            insertExpenseButton.invisible()
            progressBarImageUpdate.visible()
            viewModel.insertExpenseImageAttach(imageUri)
            initAttachObserver()
        }
    }

    private fun initAttachObserver() {
        viewModel.apply {
            this.updateExpenseImageURLOnLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it != null) {
                    imageUrl = it
                    if (!isValidUrl(imageUrl)) {
                        Snackbar.make(requireView(), getString(R.string.image_upload_fail_snackbar_message), BaseTransientBottomBar.LENGTH_SHORT).show()
                    }
                    progressBarImageUpdate.invisible()
                    insertExpenseButton.visible()
                }
            })
        }
    }

    private fun setupDatePickerDialogListener() {
        dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
        descriptionInputEditText.text = safeArgs.description.toEditable()
        wasPaidCheckBox.isChecked = safeArgs.wasPaid
        val date = safeArgs.date?.toDate()
        dateSelectedOnDatePickerTextView.text = formatDateWithSeconds(date)
        if (isValidUrl(safeArgs.imageUri)) {
            insertedImage.loadImageUrl(safeArgs.imageUri)
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}
