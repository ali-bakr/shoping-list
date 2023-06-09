package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ShoppingItemAdapter
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()

    @Inject
    lateinit var testShoppingFragmentFactory: TestShoppingFragmentFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }


    @Test
    fun swipeItemToDeleteInDB() {
        val shoppingItem = ShoppingItem("TEST", 1, 1f, "")
        var testViewModel: ShoppingViewModel? = null

        launchFragmentInHiltContainer<ShoppingFragment>(fragmentFactory = testShoppingFragmentFactory) {

            testViewModel = shoppingViewModel
            shoppingViewModel?.insertShoppingItemToDB(shoppingItem)
        }

        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                swipeLeft()
            )
        )

        assertThat(testViewModel?.shoppingItems?.getOrAwaitValue ()).isEmpty()

    }

    @Test
    fun clickAddShoppingItemButton_NavigateToAddShoppingItemFragment() {
        val navController = mock(NavController::class.java)
//        `when`(navController.popBackStack()).thenReturn(false)
        launchFragmentInHiltContainer<ShoppingFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.fabAddShoppingItem)).perform(click())
        verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingFragment()
        )
    }


}