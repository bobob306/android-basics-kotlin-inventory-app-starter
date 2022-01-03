package com.benb.inventory.data.basket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.util.*

@Entity(tableName = "basket")
data class Basket(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val bItemName: String,
    @ColumnInfo(name = "price")
    val bItemPrice: Double,
    @ColumnInfo (name = "shop")
    val bShop: String,
    @ColumnInfo (name = "quantity")
    val bItemQuantity: Int = 1,
)

fun Basket.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance(Locale.GERMANY).format(bItemPrice)

fun Basket.getValue(): Double =
    bItemPrice.times(bItemQuantity)