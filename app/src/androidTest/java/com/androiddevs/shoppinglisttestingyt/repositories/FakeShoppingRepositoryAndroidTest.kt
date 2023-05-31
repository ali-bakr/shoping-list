package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.Resource
import com.androiddevs.shoppinglisttestingyt.repository.ShoppingRepository

class FakeShoppingRepositoryAndroidTest : ShoppingRepository {
    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    private fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshAllLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshAllLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshAllLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems

    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice

    }

    override suspend fun searchImage(imageQuery: String): Resource<ImageResponse> {

        return if (shouldReturnNetworkError){
            Resource.error("Error",null)
        }else{
            Resource.success(ImageResponse(listOf(),0,0))
        }
    }


}