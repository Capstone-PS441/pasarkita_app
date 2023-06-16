package com.rayhan.pasarkitarevision.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rayhan.pasarkitarevision.adapter.ProductSearchAdapter
import com.rayhan.pasarkitarevision.databinding.FragmentSearchBinding
import com.rayhan.pasarkitarevision.ui.beranda.BerandaFragmentDirections
import com.rayhan.pasarkitarevision.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var productAdapter: ProductSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvNoResults = binding.tvNoResults
        setupProductRecyclerView()

        val searchQuery = arguments?.getString("searchQuery")
        searchQuery?.let {
            binding.etSearch.setText(it)
            performSearch(it)
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = binding.etSearch.text.toString().trim()
                performSearch(searchQuery)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupProductRecyclerView() {
        productAdapter = ProductSearchAdapter { productId ->
            navigateToProductDetail(productId)
        }
        binding.rvSearchResults.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun performSearch(searchQuery: String) {
        viewModel.fetchSearchResults(searchQuery).observe(viewLifecycleOwner) { searchResults ->
            if (searchResults.isEmpty()) {
                binding.tvNoResults.visibility = View.VISIBLE
            } else {
                binding.tvNoResults.visibility = View.GONE
            }
            productAdapter.submitList(searchResults)
        }
    }

    fun navigateToProductDetail(productId: Int) {
        val action = SearchFragmentDirections.actionNavigationSearchToNavigationDetailProduk(productId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
