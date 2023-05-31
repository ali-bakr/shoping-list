package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao


    @Before
    fun setup() {

        hiltRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun testLaunchFragmentHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("item A", 6, 14f, "imageUrl1", 1)
        dao.insertItem(shoppingItem)
        val allShoppingItemLiveData = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItemLiveData).contains(shoppingItem)
    }


    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("item b", 4, 12f, "imageUrl2", 2)
        dao.insertItem(shoppingItem)
        dao.deleteItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }


    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("item 1", 2, 5f, "imageUrl1", 4)
        val shoppingItem2 = ShoppingItem("item 2", 5, 4f, "imageUrl2", 5)
        val shoppingItem3 = ShoppingItem("item 3", 10, 12f, "imageUrl3", 6)

        dao.insertItem(shoppingItem1)
        dao.insertItem(shoppingItem2)
        dao.insertItem(shoppingItem3)
        val totalPrice = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(2 * 5 + 5 * 4 + 10 * 12)
    }


}



