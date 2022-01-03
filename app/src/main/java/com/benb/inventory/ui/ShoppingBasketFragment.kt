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
import com.example.inventory.databinding.FragmentShoppingBasketBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ShoppingBasketFragment : Fragment() {

    @InternalCoroutinesApi
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao(), (activity?.application as InventoryApplication).database.basketDao()
        )
    }

    private var _binding: FragmentShoppingBasketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentShoppingBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allBasketItems.observe(this.viewLifecycleOwner) { basket ->
            basket.let {
                //adapter.submitList(it)
            }
        }
    }



}