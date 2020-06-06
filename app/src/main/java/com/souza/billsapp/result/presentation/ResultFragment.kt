package com.souza.billsapp.result.presentation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.souza.billsapp.databinding.FragmentResultBinding
import com.souza.billsapp.income.presentation.IncomeAdapter

class ResultFragment : Fragment(){

    private lateinit var binding: FragmentResultBinding
    private val viewModel by viewModels<ResultViewModel>()
    private lateinit var tvResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        tvResult = binding.tvResult

       initObserver()
        //tvResult.text = viewModel.i.toString()

        return binding.root
    }

    private fun initObserver() {
        viewModel.apply {
            this.updateSumResultOfExpenseOnLiveData().observe(viewLifecycleOwner, Observer {
                val obj = it

               // Handler().postDelayed({
                    tvResult.text = "${obj.toString()}"
                //}, 1000)
                }
            )
            this.updateSumResultOfIncomeOnLiveData().observe(viewLifecycleOwner, Observer {
                val obj = it

                // Handler().postDelayed({
                //tvResult.text = "${obj.toString()}"
                //}, 1000)
            }
            )
        }
    }
}
