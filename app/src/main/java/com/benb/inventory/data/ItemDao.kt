package com.benb.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query ("SELECT * FROM item WHERE id = :id" )
    fun getItem(id: Int): Flow<Item>

    @Query ("SELECT * FROM item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

    @Query ("SELECT * FROM item WHERE shop = :shop ORDER BY name ASC")
    fun getShopItems(shop: String): Flow<List<Item>>

}