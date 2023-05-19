package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.interfaces.RSAMultiPrimePrivateCrtKey
import java.time.temporal.TemporalAmount

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    var name :String,
    var amount: Int,
    var price :Float,
    var imgUrl:String,
    @PrimaryKey(autoGenerate = true)
    val id:Int
)
//@Entity(tableName = "shopping_items")
//data class ShoppingItem(
//    var name: String,
//    var amount: Int,
//    var price: Float,
//    var imageUrl: String,
//    @PrimaryKey(autoGenerate = true)
//    val id: Int? = null
//)