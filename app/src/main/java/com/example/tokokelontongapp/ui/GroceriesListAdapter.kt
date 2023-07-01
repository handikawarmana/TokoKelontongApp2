package com.example.tokokelontongapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokokelontongapp.R
import com.example.tokokelontongapp.model.Groceries

class GroceriesListAdapter(
    private val onItemClickListener: (Groceries) -> Unit
): ListAdapter<Groceries, GroceriesListAdapter.GroceriesViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceriesViewHolder {
        return GroceriesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GroceriesViewHolder, position: Int) {
        val groceries = getItem(position)
        holder.bind(groceries)
        holder.itemView.setOnClickListener {
            onItemClickListener(groceries)
        }
    }

    class GroceriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)

        fun bind(groceries: Groceries?) {
            nameTextView.text = groceries?.name
            addressTextView.text = groceries?.address
            numberTextView.text = groceries?.number
        }

        companion object {
            fun create(parent: ViewGroup): GroceriesListAdapter.GroceriesViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_groceries, parent, false)
                return GroceriesViewHolder(view)
            }
        }
    }
    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Groceries>(){
            override fun areItemsTheSame(oldItem: Groceries, newItem: Groceries): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Groceries, newItem: Groceries): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}