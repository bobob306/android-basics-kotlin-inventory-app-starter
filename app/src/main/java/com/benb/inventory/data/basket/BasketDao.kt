package com.benb.inventory.data.basket

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(basket: Basket)

    @Update
    suspend fun update(basket: Basket)

    @Delete
    suspend fun delete(basket: Basket)

    @Query("SELECT * FROM basket ORDER BY name ASC")
    fun getBasketContents(): Flow<List<Basket>>
}