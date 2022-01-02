package com.benb.inventory

import android.util.Log
import androidx.lifecycle.*
import com.benb.inventory.data.Item
import com.benb.inventory.data.ItemDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

enum class SortOrder {BY_NAME, BY_SHOP}

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    private var _basketPrice = MutableLiveData<Double>(0.0)
    val basketPrice: LiveData<Double> get() = _basketPrice

    private var _basketContents = MutableLiveData<List<String>>()
    val basketContents: LiveData<List<String>> get() = _basketContents

    val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow(SortOrder.BY_SHOP)

    private val itemsFlow = combine(
        searchQuery,
        sortOrder
    ) {
        query, sortOrder ->
        Pair(query, sortOrder)
    }
        .flatMapLatest { (query, sortOrder) ->
        itemDao.getItems(query, sortOrder)
    }

    val allItems: LiveData<List<Item>> = itemsFlow.asLiveData()

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

    fun onLineClicked(item: Item) {
        // navigate to item detail fragment, currently already implemented in item list fragment
    }

    fun onCartClicked(item: Item) {
        _basketPrice.value = _basketPrice.value?.plus(item.itemPrice)
        _basketContents.value = _basketContents.value?.plusElement(item.itemName) ?: listOf(item.itemName)
        Log.d("Basket price checker","Basket subtotal = €${basketPrice.value.toString()}")
        Log.d("Basket content checker", "Basket content = ${basketContents.value!!.last()}")
    }

    fun goToBasket() {
        // navigate to basket fragment
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