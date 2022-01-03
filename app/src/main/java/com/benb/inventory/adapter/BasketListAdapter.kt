package com.benb.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.benb.inventory.data.basket.Basket
import com.benb.inventory.data.basket.getFormattedPrice
import com.example.inventory.databinding.BasketListItemBinding

class BasketListAdapter(private val listener: OnBasketClickListener) :
    ListAdapter<Basket, BasketListAdapter.BasketViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Basket>() {
            override fun areContentsTheSame(oldItem: Basket, newItem: Basket): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Basket, newItem: Basket): Boolean {
                return oldItem.bItemName == newItem.bItemName
            }
        }

    }

    inner class BasketViewHolder(private var binding: BasketListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                basketRemove.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val basket = getItem(position)
                        listener.onDeleteClicked(basket)
                    }
                }
                basketQuantAdd.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val basket = getItem(position)
                        listener.onPlusClicked(basket)
                    }
                }
                basketQuantMinus.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val basket = getItem(position)
                        listener.onMinusClicked(basket)
                    }
                }
            }
        }

        fun bind(basket: Basket) {
            binding.apply {
                basketName.text = basket.bItemName
                basketPrice.text = basket.getFormattedPrice()
                basketQuantity.text = basket.bItemQuantity.toString()
                basketShop.text = basket.bShop
            }
        }

    }


    interface OnBasketClickListener {
        fun onPlusClicked(basket: Basket)
        fun onMinusClicked(basket: Basket)
        fun onDeleteClicked(basket: Basket)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(BasketListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

}