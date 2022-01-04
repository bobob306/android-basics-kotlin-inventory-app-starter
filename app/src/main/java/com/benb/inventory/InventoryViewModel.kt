package com.benb.inventory

import android.util.Log
import androidx.lifecycle.*
import com.benb.inventory.data.basket.Basket
import com.benb.inventory.data.basket.BasketDao
import com.benb.inventory.data.item.Item
import com.benb.inventory.data.item.ItemDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

enum class SortOrder {BY_NAME, BY_SHOP}

class InventoryViewModel(private val itemDao: ItemDao, private val basketDao: BasketDao) : ViewModel() {

    private var _basketPrice = MutableLiveData<Double>(0.0)
    val basketPrice: LiveData<Double> get() = _basketPrice

    private var _basketContents = MutableLiveData<List<String>>()
    val basketContents: LiveData<List<String>> get() = _basketContents

    private var _basket = MutableLiveData<List<Item>>()
    val basket: LiveData<List<Item>> get() = _basket
    private var _basketSubtotal = MutableLiveData<Double>()
    val basketSubtotal get()= _basketSubtotal

    val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow(SortOrder.BY_SHOP)

    private val itemEventChannel = Channel<ItemEvent>()
    val itemEvent = itemEventChannel.receiveAsFlow()

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
    val allBasketItems: LiveData<List<Basket>> = basketDao.getBasketContents().asLiveData()

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }
    private fun insertBasket(basket: Basket) {
        viewModelScope.launch {
            basketDao.insert(basket)
        }
    }

    private fun getNewItemEntry(itemName: String,itemPrice: String,shop: String,itemQuantity: String): Item {
        // val itemStockValue = (itemPrice.toDouble()*shop.toInt()*0.5).toString()
        return Item (
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            shop = shop,
            itemQuantity = itemQuantity.toDouble()
        )
    }
    private fun createNewBasketEntry(itemName: String, itemPrice: Double, shop: String, itemQuantity: Int = 1) : Basket{
        return Basket(bItemName = itemName, bItemPrice = itemPrice, bShop = shop, bItemQuantity = 1)

    }

    fun addItemToBasket(itemName: String, itemPrice: Double, shop: String, itemQuantity: Int = 1) {
        val newBasketEntry = createNewBasketEntry(itemName, itemPrice, shop, itemQuantity)
        insertBasket(newBasketEntry)

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

    fun getUpdatedItemEntry(itemId: Int,itemName: String,itemPrice: String,shop: String,itemQuantity: String) : Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            shop = shop,
            itemQuantity = itemQuantity.toDouble()
        )
    }

    fun updateItem(itemId: Int,itemName: String,itemPrice: String,shop: String,itemQuantity: String) {
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
        _basket.value = _basket.value?.plusElement(item) ?: listOf(item)
        _basketPrice.value = _basketPrice.value?.plus(item.itemPrice)
        addItemToBasket(item.itemName, item.itemPrice, item.shop)

        //_basketContents.value = _basketContents.value?.plusElement(item.itemName) ?: listOf(item.itemName)
        Log.d("Basket checker","Basket subtotal = ${basket.value!!.last().itemName} €${basket.value!!.last().itemPrice}")

        //Log.d("Basket content checker", "Basket content = ${basketContents.value!!.last()}")
    }

    fun onPlusClicked(basket: Basket) {
        // add one to basket item quantity
    }
    fun onMinusClicked(basket: Basket) {
        // subtract one to basket item quantity
        // if one show delete confirmation and dialogue and delete item
    }

    fun onDeleteClicked(basket: Basket) {
        viewModelScope.launch {
            basketDao.delete(basket)
        }
    }

    fun  onUndoDeleteClick(item: Item) = viewModelScope.launch {
        itemDao.insert(item)
    }

    sealed class ItemEvent {
        data class ShowUndoDeleteMessage(val item: Item) : ItemEvent()
    }

}

class InventoryViewModelFactory(private val itemDao: ItemDao, private val basketDao: BasketDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress ("UNCHECKED_CAST")
            return InventoryViewModel(itemDao, basketDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}