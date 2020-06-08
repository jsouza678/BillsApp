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
import com.souza.billsapp.sharedextensions.formatDateWithSeconds
import com.souza.billsapp.sharedextensions.formatValueToCoin
import com.souza.billsapp.sharedextensions.invisible
import com.souza.billsapp.sharedextensions.visible

class ExpenseAdapter(options: FirestoreRecyclerOptions<Expense>) : FirestoreRecyclerAdapter<Expense,
        ExpenseAdapter.ExpenseViewHolder>(options) {

    private var listener: OnItemClickListener? = null
    private var listenerLong: OnItemLongClickListener? = null

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
        private val valueTextView: TextView = itemView.findViewById(R.id.value_text_view_expense_item)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description_value_text_view_expense_item)
        private val dateTextView: TextView = itemView.findViewById(R.id.date_value_text_view_income_item)
        private val wasPaidSwitch: Switch = itemView.findViewById(R.id.was_paid_switch_expense_item)
        private val attachImageView: ImageView = itemView.findViewById(R.id.attach_icon_image_view_expense_item)

        fun itemBind(expense: Expense) {
            valueTextView.text = expense.value?.let { formatValueToCoin(it) }
            descriptionTextView.text = expense.description
            val timeStampAsDateResult = expense.date?.toDate()
            dateTextView.text = formatDateWithSeconds(timeStampAsDateResult)
            wasPaidSwitch.isChecked = expense.wasPaid
            if (expense.imageUri.isEmpty()) {
                attachImageView.invisible()
            } else {
                attachImageView.visible()
            }
        }

        val onClickListener = itemView.setOnClickListener {
            val position: Int = adapterPosition
            // Avoid a crash while touching a item while it is being removed
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener!!.onItemClick(snapshots.getSnapshot(position), position)
            }
        }

        val onLongClickListener = itemView.setOnLongClickListener {
            val position: Int = adapterPosition
            // Avoid a crash while touching a item while it is being removed
            if (position != RecyclerView.NO_POSITION && listenerLong != null) {
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
