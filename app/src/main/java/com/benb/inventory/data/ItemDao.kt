package com.benb.inventory.data

import androidx.room.*
import com.benb.inventory.SortOrder
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

    fun getItems(query: String, sortOrder: SortOrder) : Flow<List<Item>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getItemsSortedByName(query)
            SortOrder.BY_SHOP -> getItemsSortedByShop(query)
        }

    @Query ("SELECT * FROM item WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name DESC ")
    fun getItemsSortedByName(searchQuery: String): Flow<List<Item>>
    // https://www.youtube.com/watch?v=dd_Lv7AxqkY 7:18

    @Query ("SELECT * FROM item WHERE name LIKE '%' || :searchQuery || '%' ORDER BY shop DESC ")
    fun getItemsSortedByShop(searchQuery: String): Flow<List<Item>>

}