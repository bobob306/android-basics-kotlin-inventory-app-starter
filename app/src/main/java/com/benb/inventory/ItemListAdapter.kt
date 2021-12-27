package com.benb.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.benb.inventory.data.Item
import com.benb.inventory.data.getFormattedPrice
import com.benb.inventory.databinding.ItemListItemBinding

class ItemListAdapter(private val onItemClicked: (Item) -> Unit) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(internal var binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.apply {
                itemName.text = item.itemName
                itemPrice.text = item.getFormattedPrice()
                itemShop.text = item.shop.toString()
                itemShop.setOnClickListener{
                    val shopName = itemShop.text.toString()
                    Toast.makeText(root.context, "Clicked: ${item.shop}", Toast.LENGTH_SHORT ).show()

                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName == newItem.itemName
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListAdapter.ItemViewHolder {

        return ItemViewHolder(ItemListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ItemListAdapter.ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.binding.itemShop.setOnClickListener {
            onItemClicked(current)
            val shopName = current.shop
            fun showShopList(shopName: String) {
                val shopName = holder.binding.itemShop.text.toString()
                val action = ItemListFragmentDirections.actionItemListFragmentToShopItemFragment(shopName)

            }
        }
        holder.bind(current)
    }

}