package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ImageAdapter
import com.androiddevs.shoppinglisttestingyt.other.Constants.GRID_SPAN_COUNT
import kotlinx.android.synthetic.main.fragment_image_pick.*
import javax.inject.Inject

class ImagePickFragment @Inject constructor(
     val imageAdapter:ImageAdapter
) :Fragment(R.layout.fragment_image_pick) {
    lateinit var shoppingViewModel: ShoppingViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingViewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        setUpRecyclerView()

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            shoppingViewModel.setImageUrl(it)
        }

    }

    private  fun setUpRecyclerView(){
        rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(),GRID_SPAN_COUNT)
        }
    }
}