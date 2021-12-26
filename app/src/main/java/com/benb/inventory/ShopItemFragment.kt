package com.benb.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.benb.inventory.data.Item
import com.benb.inventory.databinding.FragmentShopItemBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ShopItemFragment : Fragment() {

    private val navigationArgs: ShopItemFragmentArgs by navArgs()

    @InternalCoroutinesApi
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentShopItemBinding? = null
    val binding get()= _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter {
            val action = ShopItemFragmentDirections.actionShopItemFragmentToItemDetailFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter

        val shop = navigationArgs.shop
        val shopItems: LiveData<List<Item>> = viewModel.retrieveShopItems(shop)
        shopItems.observe(this.viewLifecycleOwner) {items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }


}