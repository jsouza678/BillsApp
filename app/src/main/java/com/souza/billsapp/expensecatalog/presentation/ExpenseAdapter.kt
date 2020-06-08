package com.souza.billsapp.expensecatalog.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.souza.billsapp.R
import com.souza.billsapp.expensecatalog.domain.Expense
import com.souza.billsapp.extensions.formatValueToCoin
import com.souza.billsapp.extensions.invisible
import com.souza.billsapp.extensions.visible
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class ExpenseAdapter(options: FirestoreRecyclerOptions<Expense>) : FirestoreRecyclerAdapter<Expense,
        ExpenseAdapter.ExpenseViewHolder>(options) {

    private var listener : OnItemClickListener? = null
    private var listenerLong : OnItemLongClickListener? = null

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
        private val valor : TextView = itemView.findViewById(R.id.value_text_view_expense_item)
        private val descricao : TextView = itemView.findViewById(R.id.description_value_text_view_expense_item)
        private val data : TextView = itemView.findViewById(R.id.date_value_text_view_income_item)
        private val pago : Switch = itemView.findViewById(R.id.was_paid_switch_expense_item)
        private val attach : ImageView = itemView.findViewById(R.id.attach_icon_image_view_expense_item)

        fun itemBind(expense: Expense) {
            valor.text = expense.value?.let { formatValueToCoin(it) }
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

        val onLongClickListener = itemView.setOnLongClickListener { view ->
            val position : Int = adapterPosition
            // Avoid a crash while touching a item while it is being removed
            if(position != RecyclerView.NO_POSITION && listenerLong != null) {
                listenerLong!!.onItemClick(snapshots.getSnapshot(position), position)
            }
            false
        }
    }

    interface OnItemClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.listener = onItemClickListener
    }

    interface OnItemLongClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.listenerLong = onItemLongClickListener
    }
}
