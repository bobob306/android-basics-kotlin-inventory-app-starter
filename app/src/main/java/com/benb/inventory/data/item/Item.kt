package com.benb.inventory.data.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.util.*

@Entity(tableName = "item")
data class Item (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo (name = "name")
    val itemName: String,
    @ColumnInfo (name = "price")
    val itemPrice: Double,
    @ColumnInfo (name = "shop")
    val shop: String,
    @ColumnInfo (name = "quantity")
    val itemQuantity: Double = 1.00,
        )

fun Item.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance(Locale.GERMANY).format(itemPrice)

fun Item.getValue(): Double =
    itemPrice.div(itemQuantity)