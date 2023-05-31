package com.androiddevs.shoppinglisttestingyt.ui

import android.content.ClipData.Item
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ShoppingItemAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_shopping.*
import javax.inject.Inject

class ShoppingFragment @Inject constructor(
    val shoppingItemAdapter: ShoppingItemAdapter,
    var shoppingViewModel:ShoppingViewModel? = null
) : Fragment(R.layout.fragment_shopping) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObserver()
        setUpRecyclerView()

        shoppingViewModel =shoppingViewModel?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)


        fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(
                ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingFragment()
            )
        }

    }

    private val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[position]
            shoppingViewModel?.deleteShoppingItem(item)
            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    shoppingViewModel?.insertShoppingItemToDB(item)
                }
                show()
            }
        }

    }

    fun subscribeToObserver() {
        shoppingViewModel?.shoppingItems?.observe(viewLifecycleOwner, Observer {
            shoppingItemAdapter.shoppingItems = it
        })
        shoppingViewModel?.totalPrice?.observe(viewLifecycleOwner, Observer {
            val price = it ?: 0f
            val priceText = "Price Text : $price$ "
            tvShoppingItemPrice.text = priceText
        })
    }

    private fun setUpRecyclerView() {
        rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchHelper).attachToRecyclerView(this)
        }
    }
}