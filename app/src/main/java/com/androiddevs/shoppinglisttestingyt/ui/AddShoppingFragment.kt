package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ImageAdapter
import com.androiddevs.shoppinglisttestingyt.other.Status
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import javax.inject.Inject

class AddShoppingFragment @Inject constructor(
    private val glide:RequestManager
) :Fragment(R.layout.fragment_add_shopping_item) {
    lateinit var shoppingViewModel: ShoppingViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingViewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObserver()

        btnAddShoppingItem.setOnClickListener{
            shoppingViewModel.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }

        ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingFragmentDirections.actionAddShoppingFragmentToImagePickFragment()
            )
        }

        val  callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                shoppingViewModel.setImageUrl("")
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun subscribeToObserver(){
        shoppingViewModel.currentUrlImages.observe(viewLifecycleOwner,Observer{
            glide.load(it).into(ivShoppingImage)
        })

        shoppingViewModel.insertShoppingItemStatus.observe(viewLifecycleOwner,Observer{
            it.getContentIfNotHandled()?.let {result ->
                when(result.status){
                    Status.SUCCESS ->{
                        Snackbar.make(requireActivity().rootLayout,
                        "add shopping Item",
                        Snackbar.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR->{
                        Snackbar.make(requireActivity().rootLayout,
                            result.message?:"unknown error occured",
                            Snackbar.LENGTH_LONG).show()

                    }
                    Status.LOADING->{

                    }
                }
            }
        })
    }
}