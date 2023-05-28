package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.MainCoroutineRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.androiddevs.shoppinglisttestingyt.other.Constants
import com.androiddevs.shoppinglisttestingyt.other.Status
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepositories
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi


import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule =MainCoroutineRule()

    lateinit var viewModel:ShoppingViewModel
    @Before
    fun setUp() {
    viewModel= ShoppingViewModel(FakeShoppingRepositories())
    }

    @Test
    fun `insert shopping item with empty field , return error`(){
        viewModel.insertShoppingItem("any name","","")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name , returns error`(){
       val string = buildString {
           for (i in 1..Constants.MAX_NAME_LENGTH+1){
               append(1)
           }
       }
        viewModel.insertShoppingItem("any nameany nameany nameany","6","5.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high price , returns error`(){
        val string = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH+1){
                append(1)
            }
        }
        viewModel.insertShoppingItem("nameany","6",string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount , returns error`(){
        viewModel.insertShoppingItem(" nameany","999999999999999999","5.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input , returns success`(){
        viewModel.insertShoppingItem(" nameany","9999","5.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}