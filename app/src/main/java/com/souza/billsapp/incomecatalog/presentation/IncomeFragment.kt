package com.souza.billsapp.incomecatalog.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.google.firebase.firestore.DocumentSnapshot
import com.souza.billsapp.R
import com.souza.billsapp.databinding.FragmentIncomesBinding
import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.incomecatalog.utils.Constants.Companion.ABSOLUTE_ZERO
import com.souza.billsapp.sharedextensions.isValidUrl
import com.souza.billsapp.sharedextensions.loadImageUrl
import org.koin.android.viewmodel.ext.android.viewModel

class IncomeFragment : Fragment() {

    private lateinit var binding: FragmentIncomesBinding
    private lateinit var insertButton: FloatingActionButton
    private lateinit var recyclerAdapter: IncomeAdapter
    private lateinit var incomesRecyclerView: RecyclerView
    private val viewModel by viewModel<IncomeCatalogViewModel>()
    private lateinit var navController: NavController
    private lateinit var filterMenu: Menu
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.income_fragment_title)
        binding = DataBindingUtil.inflate<FragmentIncomesBinding>(inflater,
            R.layout.fragment_incomes,
            container,
            false)

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        insertButton = binding.insertIncomeFloatingActionButtonIncomesFragment
        incomesRecyclerView = binding.incomesRecyclerViewIncomesFragment

        setupDialog()
        initObserver()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        // INSERT
        insertButton.setOnClickListener {
            navController.navigate(R.id.action_incomeFragment_to_insertIncomeFragment)
        }
    }

    private fun initObserver() {
        viewModel.apply {
            this.dataList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                recyclerAdapter = IncomeAdapter(it)
                initRecyclerView()
                recyclerAdapter.startListening()
            })
        }
    }

    private fun initRecyclerView() {
        incomesRecyclerView.setHasFixedSize(true)
        incomesRecyclerView.layoutManager = LinearLayoutManager(context)
        incomesRecyclerView.adapter = recyclerAdapter

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
        itemTouchHelper.attachToRecyclerView(incomesRecyclerView)

        recyclerAdapter.setOnItemClickListener(object : IncomeAdapter.OnItemClickListener {
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {

                val income = documentSnapshot.toObject(Income::class.java)
                if (income != null) {
                    if (!income.imageUri.isBlank()) {
                        val imageViewAttach = dialog.findViewById(R.id.image_attached_image_view_dialog) as ImageView
                        if (isValidUrl(income.imageUri)) {
                            imageViewAttach.loadImageUrl(income.imageUri)
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

        recyclerAdapter.setOnItemLongClickListener(object : IncomeAdapter.OnItemLongClickListener {
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
                val income = documentSnapshot.toObject(Income::class.java)
                if (income != null) {
                    val documentId = documentSnapshot.id
                    val valueToUpdate: Float = income.value!!
                    val descriptionToUpdate: String = income.description!!
                    val wasReceivedToUpdate: Boolean = income.wasReceived
                    val dateToUpdate: Timestamp = income.date!!
                    val imageUri: String = income.imageUri
                    val actionDetail = IncomeFragmentDirections
                        .actionIncomeFragmentToUpdateIncomeFragment(
                            documentId = documentId,
                            value = valueToUpdate,
                            description = descriptionToUpdate,
                            wasReceived = wasReceivedToUpdate,
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
                navController.navigate(R.id.action_incomeFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
