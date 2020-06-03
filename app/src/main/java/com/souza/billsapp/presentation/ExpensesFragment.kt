package com.souza.billsapp.presentation

import android.media.MediaRouter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.souza.billsapp.R
import com.souza.billsapp.data.Expense
import com.souza.billsapp.databinding.FragmentExpensesBinding
import java.util.*
import kotlin.math.exp

class ExpensesFragment : Fragment(){

    private lateinit var binding : FragmentExpensesBinding
    private lateinit var insertButton : FloatingActionButton
    private lateinit var recyclerAdapter : ExpenseAdapter
    private lateinit var expensesRecyclerView : RecyclerView
    private val viewModel by viewModels<ExpenseViewModel>()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Gastos"
        binding = DataBindingUtil.inflate<FragmentExpensesBinding>(inflater,
            R.layout.fragment_expenses,
            container,
            false)

        (activity as AppCompatActivity).supportActionBar?.hide()
        insertButton = binding.insertExpenseButton
        expensesRecyclerView = binding.expensesRecyclerViewExpensesFragment
        initObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        //INSERT
        insertButton.setOnClickListener {
            navController.navigate(R.id.action_billFragment_to_insertExpenseFragment)
        }
        initObserver()
    }

    private fun initObserver() {
        viewModel.apply {
            this.dataList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                recyclerAdapter= ExpenseAdapter(it)
                initRecyclerView()
                recyclerAdapter.startListening()
            })
        }
    }

    private fun initRecyclerView() {
        expensesRecyclerView.setHasFixedSize(true)
        expensesRecyclerView.layoutManager = LinearLayoutManager(context)
        expensesRecyclerView.adapter = recyclerAdapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
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

        recyclerAdapter.setOnItemClickListener(object: ExpenseAdapter.OnItemClickListener{
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
                    val expense = documentSnapshot.toObject(Expense::class.java)
                    if(expense!= null){
                        val documentId = documentSnapshot.id
                        val valueToUpdate: Int = expense.value!!
                        val descriptionToUpdate: String = expense.description!!
                        val wasPaidToUpdate: Boolean = expense.wasPaid
                        val dateToUpdate: Timestamp = expense.date!!
                        val actionDetail = ExpensesFragmentDirections
                            .actionBillFragmentToUpdateExpenseFragment(
                                documentId,
                                valueToUpdate,
                                descriptionToUpdate,
                                wasPaidToUpdate,
                                dateToUpdate
                            )
                        navController.navigate(actionDetail)
                    }
                }
            }
        )
    }


}
