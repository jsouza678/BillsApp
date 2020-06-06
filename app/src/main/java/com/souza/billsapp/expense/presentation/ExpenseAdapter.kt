package com.souza.billsapp.expense.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.souza.billsapp.R
import com.souza.billsapp.data.Expense
import com.souza.billsapp.extensions.invisible
import com.souza.billsapp.extensions.visible
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class ExpenseAdapter(options: FirestoreRecyclerOptions<Expense>) : FirestoreRecyclerAdapter<Expense,
        ExpenseAdapter.ExpenseViewHolder>(options) {

    private var listener : OnItemClickListener? = null

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int, model: Expense) {
        holder.itemBind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.expense_item,
            parent,
            false
        )
        return ExpenseViewHolder(view)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val valor : TextView = itemView.findViewById(R.id.valor_valor)
        private val descricao : TextView = itemView.findViewById(R.id.desc_valor)
        private val data : TextView = itemView.findViewById(R.id.data_valor)
        private val pago : Switch = itemView.findViewById(R.id.pago_switch)
        private val attach : ImageView = itemView.findViewById(R.id.icone_image_view)

        fun itemBind(expense: Expense) {
            valor.text = expense.value.toString()
            descricao.text = expense.description
            val timeStampAsDateResult = expense.date?.toDate()
            data.text = formatDate(timeStampAsDateResult)
            pago.isChecked = expense.wasPaid
            if(expense.imageUri.isEmpty()) {
                attach.invisible()
            }else{
                attach.visible()
            }
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
