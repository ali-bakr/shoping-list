package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [ShoppingItem::class],
    version = 1
)
public abstract class ShoppingItemDatabase : RoomDatabase() {
    abstract fun shoppingDao(): ShoppingDao
}


