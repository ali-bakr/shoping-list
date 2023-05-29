package com.androiddevs.shoppinglisttestingyt.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.Constants
import com.androiddevs.shoppinglisttestingyt.other.Event
import com.androiddevs.shoppinglisttestingyt.other.Resource
import com.androiddevs.shoppinglisttestingyt.repository.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingViewModel  @ViewModelInject   constructor(
    val shoppingRepository: ShoppingRepository
) :ViewModel(){

    val shoppingItems=shoppingRepository.observeAllShoppingItems()
    val totalPrice=shoppingRepository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images:LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentUrlImages = MutableLiveData<String>()
    val currentUrlImages:LiveData<String> = _currentUrlImages

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus:LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    private val _insertShoppingItem = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItem:LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItem


    fun setImageUrl(url:String){
        _currentUrlImages.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem)=viewModelScope.launch {
     shoppingRepository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemToDB(shoppingItem: ShoppingItem)=viewModelScope.launch{
        shoppingRepository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name:String,amountString:String,priceString:String){

        if(name.isEmpty()||amountString.isEmpty()||priceString.isEmpty()){
            _insertShoppingItemStatus.postValue(Event(Resource.error("fields must not be empty",null)))
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error("name must not exceed ${Constants.MAX_NAME_LENGTH} chars",null)))
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error("price must not exceed ${Constants.MAX_PRICE_LENGTH} digits",null)))
            return
        }

        val amount=try {
            amountString.toInt()
        }catch (e:Exception){

            _insertShoppingItemStatus.postValue(Event(Resource.error("please enter a valid amount",null)))
            return
        }

        val shoppingItem=ShoppingItem(name,amount,priceString.toFloat(),_currentUrlImages.value ?:"")
        insertShoppingItemToDB(shoppingItem)
        setImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }
    fun searchForIMage(imageQuery:String){

        if (imageQuery.isEmpty()){
            return
        }
        _images.value= Event(Resource.loading(null))
        viewModelScope.launch {

            val response=shoppingRepository.searchImage(imageQuery)
            _images.value=Event(response)

        }
    }


}
