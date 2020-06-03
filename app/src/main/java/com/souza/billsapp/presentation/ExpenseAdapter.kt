package com.souza.billsapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.souza.billsapp.R
import com.souza.billsapp.data.Expense

class ExpenseAdapter(options: FirestoreRecyclerOptions<Expense>) : FirestoreRecyclerAdapter<Expense,
        ExpenseAdapter.ViewHolder>(options) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Expense) {
        holder.itemBind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.expense_item,
            parent,
            false
        )
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val valor : TextView = itemView.findViewById(R.id.valor_valor)
        val descricao : TextView = itemView.findViewById(R.id.desc_valor)
        val data : TextView = itemView.findViewById(R.id.data_valor)
        val pago : Switch = itemView.findViewById(R.id.pago_switch)

        fun itemBind(expense: Expense) {
            valor.text = expense.value.toString()
            descricao.text = expense.description
            data.text = expense.date.toString()
            pago.isChecked = expense.wasPaid
        }
    }
}
