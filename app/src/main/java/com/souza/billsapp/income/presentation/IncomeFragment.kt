package com.souza.billsapp.income.presentation

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
import com.souza.billsapp.data.Income
import com.souza.billsapp.databinding.FragmentIncomesBinding

class IncomeFragment : Fragment(){

    private lateinit var binding : FragmentIncomesBinding
    private lateinit var insertButton : FloatingActionButton
    private lateinit var recyclerAdapter : IncomeAdapter
    private lateinit var incomesRecyclerView : RecyclerView
    private val viewModel by viewModels<IncomeViewModel>()
    private lateinit var navController: NavController
    private var filtered : Boolean = false
    private lateinit var filterMenu : Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Entradas"
        binding = DataBindingUtil.inflate<FragmentIncomesBinding>(inflater,
            R.layout.fragment_incomes,
            container,
            false)

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        insertButton = binding.insertIncomeButton
        incomesRecyclerView = binding.incomesRecyclerViewIncomesFragment
        initObserver()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        //INSERT
        insertButton.setOnClickListener {
            navController.navigate(R.id.action_incomeFragment_to_insertIncomeFragment)
        }
    }

    private fun initObserver() {
        viewModel.apply {
            this.dataList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                recyclerAdapter= IncomeAdapter(it)
                initRecyclerView()
                recyclerAdapter.startListening()
            })
        }
    }

    private fun initRecyclerView() {
        incomesRecyclerView.setHasFixedSize(true)
        incomesRecyclerView.layoutManager = LinearLayoutManager(context)
        incomesRecyclerView.adapter = recyclerAdapter

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
        itemTouchHelper.attachToRecyclerView(incomesRecyclerView)

        recyclerAdapter.setOnItemClickListener(object: IncomeAdapter.OnItemClickListener{
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
                    val income = documentSnapshot.toObject(Income::class.java)
                    if(income!= null){
                        val documentId = documentSnapshot.id
                        val valueToUpdate: Int = income.value!!
                        val descriptionToUpdate: String = income.description!!
                        val wasReceivedToUpdate: Boolean = income.wasReceived
                        val dateToUpdate: Timestamp = income.date!!
                        val actionDetail = IncomeFragmentDirections
                            .actionIncomeFragmentToUpdateIncomeFragment(
                                documentId,
                                valueToUpdate,
                                descriptionToUpdate,
                                wasReceivedToUpdate,
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
