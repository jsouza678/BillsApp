package com.souza.billsapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.souza.billsapp.R
import com.souza.billsapp.data.Expense
import com.souza.billsapp.databinding.FragmentExpensesBinding
import java.util.*

class ExpensesFragment : Fragment() {

    private lateinit var binding : FragmentExpensesBinding
    private lateinit var insertButton : FloatingActionButton
    private val calendar = Calendar.getInstance()
    private lateinit var recyclerAdapter : ExpenseAdapter
    private lateinit var expensesRecyclerView : RecyclerView
    private val viewModel by viewModels<ExpenseViewModel>()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentExpensesBinding>(inflater,
            R.layout.fragment_expenses,
            container,
            false)

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
    }
}
