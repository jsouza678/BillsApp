package com.souza.billsapp.expense.presentation

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.souza.billsapp.R
import com.souza.billsapp.data.Expense
import com.souza.billsapp.databinding.FragmentExpensesBinding

class ExpensesFragment : Fragment(){

    private lateinit var binding : FragmentExpensesBinding
    private lateinit var insertButton : FloatingActionButton
    private lateinit var recyclerAdapter : ExpenseAdapter
    private lateinit var expensesRecyclerView : RecyclerView
    private val viewModel by viewModels<ExpenseViewModel>()
    private lateinit var navController: NavController
    private var filtered : Boolean = false
    private lateinit var filterMenu : Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Gastos"
        binding = DataBindingUtil.inflate<FragmentExpensesBinding>(inflater,
            R.layout.fragment_expenses,
            container,
            false)

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        insertButton = binding.insertExpenseButton
        expensesRecyclerView = binding.expensesRecyclerViewExpensesFragment
        initObserver()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        //INSERT
        insertButton.setOnClickListener {
            navController.navigate(R.id.action_billFragment_to_insertExpenseFragment)
        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        filterMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        when(itemId) {
            R.id.filter_icon_menu -> {
                if(item.isChecked){
                    viewModel.unfilteredListOnMLiveData()
                    item.setIcon(R.drawable.ic_filter_list)
                    item.isChecked = false
                }else{
                    item.isChecked = true
                    viewModel.filteredListOnMLiveData()
                    item.setIcon(R.drawable.ic_filter_list_black)
                }
            }
            R.id.about_icon_menu -> {
                navController.navigate(R.id.action_billFragment_to_aboutFragment)
            }
            R.id.exit_icon_menu -> {
                AuthUI.getInstance().signOut(requireContext())
                navController.navigate(R.id.action_billFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
