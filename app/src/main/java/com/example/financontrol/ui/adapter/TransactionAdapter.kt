package com.example.financontrol.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financontrol.R
import com.example.financontrol.data.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.financontrol.utils.MoneyFormatter


class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.VH>() {

    private var data: List<Transaction> = emptyList()

    fun submit(list: List<Transaction>) {
        data = list
        notifyDataSetChanged()
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val tx = data[position]
        holder.tvTitle.text = "${tx.type} - ${MoneyFormatter.formatCLP(tx.amount)}"
        holder.tvDesc.text = tx.description

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.tvDate.text = sdf.format(Date(tx.dateMillis))
    }
}
