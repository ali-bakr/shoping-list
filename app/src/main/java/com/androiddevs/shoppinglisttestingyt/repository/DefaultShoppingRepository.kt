package com.androiddevs.shoppinglisttestingyt.repository

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDao
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.PixabayAPI
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.Resource

import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertItem(shoppingItem)

    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {

        shoppingDao.deleteItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {

        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {

        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchImage(imageQuery: String): Resource<ImageResponse> {

        return try {
            val response=pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful){
              response.body()?.let {
                  return@let Resource.success(it)
              } ?: Resource.error("unknown error occurred",null)
            }else{
                return Resource.error("unknown error occurred",null)
            }
        } catch (e: Exception) {
            return Resource.error("Could not reach the server", null)
        }
    }
}