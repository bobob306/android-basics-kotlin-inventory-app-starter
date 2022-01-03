/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benb.inventory.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benb.inventory.InventoryApplication
import com.benb.inventory.InventoryViewModel
import com.benb.inventory.InventoryViewModelFactory
import com.benb.inventory.SortOrder
import com.benb.inventory.adapter.ItemListAdapter
import com.benb.inventory.data.item.Item
import com.benb.inventory.util.onQueryTextChanged
import com.example.inventory.R
import com.example.inventory.databinding.ItemListFragmentBinding
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Main fragment displaying details for all items in the database.
 */
@InternalCoroutinesApi
class ItemListFragment : Fragment(), ItemListAdapter.onItemClickListener {

    @InternalCoroutinesApi
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao(), (activity?.application as InventoryApplication).database.basketDao()
        )
    }

    private var _binding: ItemListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ItemListAdapter(onItemClicked = {
            val action = ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id)
            this.findNavController().navigate(action)
        }, this)
        binding.recyclerView.adapter = adapter

        viewModel.allItems.observe(this.viewLifecycleOwner) {items ->
            items.let {
                adapter.submitList(it)
            }
        }

        viewModel.basketPrice.observe(this.viewLifecycleOwner) { cartPrice ->
            cartPrice.let {
                binding.cartTotalPrice.text = "Cart Total: € ${viewModel.basketPrice.value.toString()}"
            }
        }

        binding.shoppingBasketButton.setOnClickListener{
            viewModel.goToBasket()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.floatingActionButton.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_list, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            R.id.action_sort_by_shop -> {
                viewModel.sortOrder.value = SortOrder.BY_SHOP
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCartClicked(item: Item) {
        viewModel.onCartClicked(item)
    }

    override fun onLineClicked(item: Item) {
        viewModel.onLineClicked(item)
    }
}
