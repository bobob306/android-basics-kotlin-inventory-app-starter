package com.benb.inventory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.benb.inventory.InventoryApplication
import com.benb.inventory.InventoryViewModel
import com.benb.inventory.InventoryViewModelFactory
import com.example.inventory.R
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ShoppingBasketFragment : Fragment() {

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_basket, container, false)
    }

}