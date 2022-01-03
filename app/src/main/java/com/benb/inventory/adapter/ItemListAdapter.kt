package com.benb.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.benb.inventory.data.item.Item
import com.benb.inventory.data.item.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding

class ItemListAdapter(private val onItemClicked: (Item) -> Unit, private val listener: onItemClickListener) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    inner class ItemViewHolder(private var binding: ItemListItemBinding):RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                addToCart.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onCartClicked(item)
                    }
                }
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onLineClicked(item)
                    }
                }
            }
        }

        fun bind (item: Item) {
            binding.apply{
                itemName.text = item.itemName
                itemPrice.text = item.getFormattedPrice()
                itemShop.text = item.shop
            }
        }
    }

    interface onItemClickListener {
        fun onCartClicked(item: Item)
        fun onLineClicked(item: Item)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName ==  newItem.itemName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onItemClicked(current)
        }
        holder.bind(current)
    }

}