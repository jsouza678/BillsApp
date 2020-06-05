package com.souza.billsapp.income.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.souza.billsapp.R
import com.souza.billsapp.data.Expense
import com.souza.billsapp.data.Income
import java.text.Format
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class IncomeAdapter(options: FirestoreRecyclerOptions<Income>) : FirestoreRecyclerAdapter<Income,
        IncomeAdapter.IncomeViewHolder>(options) {

    private var listener : OnItemClickListener? = null

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int, model: Income) {
        holder.itemBind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.expense_item,
            parent,
            false
        )
        //TODO change name of expense_item
        return IncomeViewHolder(view)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    inner class IncomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val valor : TextView = itemView.findViewById(R.id.valor_valor)
        val descricao : TextView = itemView.findViewById(R.id.desc_valor)
        val data : TextView = itemView.findViewById(R.id.data_valor)
        val pago : Switch = itemView.findViewById(R.id.pago_switch)

        fun itemBind(income: Income) {
            valor.text = income.value.toString()
            descricao.text = income.description
            val timeStampAsDateResult = income.date?.toDate()
            data.text = formatDate(timeStampAsDateResult)
            pago.isChecked = income.wasReceived
        }

        private fun formatDate(date : Date?) : String{
            val formatter: Format = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
            return formatter.format(date)
        }

        val onClickListener = itemView.setOnClickListener { view ->
            val position : Int = adapterPosition
            // Avoid a crash while touching a item while it is being removed
            if(position != RecyclerView.NO_POSITION && listener != null) {
              listener!!.onItemClick(snapshots.getSnapshot(position), position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.listener = onItemClickListener
    }
}
