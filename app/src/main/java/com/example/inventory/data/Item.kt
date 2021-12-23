package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity(tableName = "item")
data class Item (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo (name = "name")
    val itemName: String,
    @ColumnInfo (name = "price")
    val itemPrice: Double,
    @ColumnInfo (name = "quantity")
    val quantityInStock: Int,
    @ColumnInfo (name = "stock value")
    val stockValue: Double = itemPrice * quantityInStock * 0.5
        )

fun Item.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(itemPrice)