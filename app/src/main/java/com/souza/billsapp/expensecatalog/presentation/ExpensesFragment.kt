package com.souza.billsapp.expensecatalog.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.souza.billsapp.R
import com.souza.billsapp.databinding.FragmentExpensesBinding
import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.expensecatalog.utils.Constants.Companion.ABSOLUTE_ZERO
import com.souza.billsapp.sharedextensions.isValidUrl
import com.souza.billsapp.sharedextensions.loadImageUrl
import org.koin.android.viewmodel.ext.android.viewModel

class ExpensesFragment : Fragment() {

    private lateinit var binding: FragmentExpensesBinding
    private lateinit var insertButton: FloatingActionButton
    private lateinit var recyclerAdapter: ExpenseAdapter
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var welcomeTextView: TextView
    private val viewModel by viewModel<ExpenseCatalogViewModel>()
    private lateinit var navController: NavController
    private lateinit var filterMenu: Menu
    private lateinit var dialog: Dialog

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Gastos"
        binding = DataBindingUtil.inflate<FragmentExpensesBinding>(inflater,
            R.layout.fragment_expenses,
            container,
            false)
        val authName: String? = FirebaseAuth.getInstance().currentUser?.displayName

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        insertButton = binding.insertExpenseFloatingActionButtonExpensesFragment
        welcomeTextView = binding.welcomeTextViewExpensesFragment
        welcomeTextView.text = "Olá $authName!"
        expensesRecyclerView = binding.expensesRecyclerViewExpensesFragment

        setupDialog()
        initObserver()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        insertButton.setOnClickListener {
            navController.navigate(R.id.action_billFragment_to_insertExpenseFragment)
        }
    }

    private fun initObserver() {
        viewModel.apply {
            this.dataList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                recyclerAdapter = ExpenseAdapter(it)
                initRecyclerView()
                recyclerAdapter.startListening()
            })
        }
    }

    private fun initRecyclerView() {
        expensesRecyclerView.setHasFixedSize(true)
        expensesRecyclerView.layoutManager = LinearLayoutManager(context)
        expensesRecyclerView.adapter = recyclerAdapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ABSOLUTE_ZERO,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                recyclerAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(expensesRecyclerView)

        recyclerAdapter.setOnItemClickListener(object : ExpenseAdapter.OnItemClickListener {
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {

                val expense = documentSnapshot.toObject(Expense::class.java)
                if (expense != null) {
                if (!expense.imageUri.isBlank()) {
                    val imageViewAttach = dialog.findViewById(R.id.image_attached_image_view_dialog) as ImageView
                    if (isValidUrl(expense.imageUri)) {
                        imageViewAttach.loadImageUrl(expense.imageUri)
                        dialog.show()
                    } else {
                        Snackbar.make(requireView(), getString(R.string.could_not_display_attached_image_snackbar_message), BaseTransientBottomBar.LENGTH_SHORT).show()
                    }
                } else {
                    Snackbar.make(requireView(), getString(R.string.no_attachs_snackbar_message_fragment), BaseTransientBottomBar.LENGTH_SHORT).show()
                }
                }
            }
        }
        )

        recyclerAdapter.setOnItemLongClickListener(object : ExpenseAdapter.OnItemLongClickListener {
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
                val expense = documentSnapshot.toObject(Expense::class.java)
                if (expense != null) {
                    val documentId = documentSnapshot.id
                    val valueToUpdate: Float = expense.value!!
                    val descriptionToUpdate: String = expense.description!!
                    val wasPaidToUpdate: Boolean = expense.wasPaid
                    val dateToUpdate: Timestamp = expense.date!!
                    val imageUri: String = expense.imageUri
                    val actionDetail = ExpensesFragmentDirections
                        .actionBillFragmentToUpdateExpenseFragment(
                            documentId = documentId,
                            value = valueToUpdate,
                            description = descriptionToUpdate,
                            wasPaid = wasPaidToUpdate,
                            date = dateToUpdate,
                            imageUri = imageUri
                        )
                    navController.navigate(actionDetail)
                }
            }
        }
        )
    }

    private fun setupDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.attached_image_preview_dialog)
        dialog.setCancelable(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        filterMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_icon_menu -> {
                if (item.isChecked) {
                    viewModel.unfilteredListOnMLiveData()
                    item.setIcon(R.drawable.ic_filter_list)
                    item.isChecked = false
                } else {
                    item.isChecked = true
                    viewModel.filteredListOnMLiveData()
                    item.setIcon(R.drawable.ic_filter_list_black)
                }
            }
            R.id.exit_icon_menu -> {
                AuthUI.getInstance().signOut(requireContext())
                navController.navigate(R.id.action_billFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
