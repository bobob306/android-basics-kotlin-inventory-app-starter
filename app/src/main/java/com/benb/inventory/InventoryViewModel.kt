package com.benb.inventory

import androidx.lifecycle.*
import com.benb.inventory.data.Item
import com.benb.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun getNewItemEntry(
        itemName: String,
        itemPrice: String,
        shop: String,
        itemQuantity: String
    ): Item {
        // val itemStockValue = (itemPrice.toDouble()*shop.toInt()*0.5).toString()
        return Item (
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            shop = shop,
            itemQuantity = itemQuantity.toDouble()
        )
    }

    fun addNewItem(itemName: String, itemPrice: String, shop: String, itemQuantity: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, shop, itemQuantity)
        insertItem(newItem)
    }

    fun isEntryValid(itemName: String, itemPrice: String, shop: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || shop.isBlank()) {
            return false }
        return true
    }

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    fun retrieveShopItems(shop: String): LiveData<List<Item>> {
        return itemDao.getShopItems(shop).asLiveData()
    }


    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        shop: String,
        itemQuantity: String
    ) : Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            shop = shop,
            itemQuantity = itemQuantity.toDouble()
        )
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        shop: String,
        itemQuantity: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, shop, itemQuantity)
        updateItem(updatedItem)
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress ("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}