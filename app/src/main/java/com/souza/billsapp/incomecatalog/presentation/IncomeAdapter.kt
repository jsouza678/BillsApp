package com.souza.billsapp.incomecatalog.presentation

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
import com.souza.billsapp.extensions.formatValueToCoin
import com.souza.billsapp.incomecatalog.domain.Income
import com.souza.billsapp.extensions.invisible
import com.souza.billsapp.extensions.visible
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class IncomeAdapter(options: FirestoreRecyclerOptions<Income>) : FirestoreRecyclerAdapter<Income,
        IncomeAdapter.IncomeViewHolder>(options) {

    private var listener : OnItemClickListener? = null
    private var listenerLong : OnItemLongClickListener? = null

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int, model: Income) {
        holder.itemBind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.income_item,
            parent,
            false
        )
        return IncomeViewHolder(view)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    inner class IncomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val valor : TextView = itemView.findViewById(R.id.value_text_view_income_item)
        private val descricao : TextView = itemView.findViewById(R.id.description_value_text_view_income_item)
        private val data : TextView = itemView.findViewById(R.id.date_value_text_view_income_item)
        private val pago : Switch = itemView.findViewById(R.id.was_paid_switch_income_item)
        private val attach : ImageView = itemView.findViewById(R.id.attach_icon_image_view_income_item)

        fun itemBind(income: Income) {
            valor.text = income.value?.let { formatValueToCoin(it) }
            descricao.text = income.description
            val timeStampAsDateResult = income.date?.toDate()
            data.text = formatDate(timeStampAsDateResult)
            pago.isChecked = income.wasReceived
            if(income.imageUri.isEmpty()) {
                attach.invisible()
            }else{
                attach.visible()
            }
        }

        private fun formatDate(date : Date?) : String{
            val formatter: Format = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
            return formatter.format(date)
        }

        val onClickListener = itemView.setOnClickListener { _ ->
            val position : Int = adapterPosition
            // Avoid a crash while touching a item while it is being removed
            if(position != RecyclerView.NO_POSITION && listener != null) {
                listener!!.onItemClick(snapshots.getSnapshot(position), position)
            }
        }

        val onLongClickListener = itemView.setOnLongClickListener { _ ->
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
