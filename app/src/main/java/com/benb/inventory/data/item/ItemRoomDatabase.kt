package com.benb.inventory.data.item

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.benb.inventory.data.basket.Basket
import com.benb.inventory.data.basket.BasketDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Basket::class, Item::class], version = 6, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun basketDao(): BasketDao

    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null
        @InternalCoroutinesApi
        fun getDatabase(context: Context): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}