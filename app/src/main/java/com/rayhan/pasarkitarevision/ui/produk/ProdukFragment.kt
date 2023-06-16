package com.rayhan.pasarkitarevision.ui.produk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rayhan.pasarkitarevision.adapter.ListProductAdapter
import com.rayhan.pasarkitarevision.databinding.FragmentProdukBinding
import com.rayhan.pasarkitarevision.util.Constant
import com.rayhan.pasarkitarevision.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProdukFragment : Fragment() , Constant.OnItemClickListener{

    private var _binding: FragmentProdukBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var recommendationAdapter: ListProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = binding.etSearch.text.toString().trim()
                performSearch(searchQuery)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        setupViewModel()
        setupRecommendationRecyclerView()
        observeLiveData()
    }

    private fun setupViewModel() {
        viewModel.recommendation.observe(viewLifecycleOwner) { recommendations ->
            Log.d("ProdukFragment", "Recommendations: $recommendations")
        }
        viewModel.fetchProducts()
    }


    private fun setupRecommendationRecyclerView() {
        recommendationAdapter = ListProductAdapter(this)
        binding.rvProduk.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(),3, LinearLayoutManager.VERTICAL, false)
            adapter = recommendationAdapter
        }
    }

    private fun observeLiveData() {
        viewModel.products.observe(requireActivity()) {
            recommendationAdapter.productList = it
        }
    }

    override fun onItemClick(productId: Int) {
        val action = ProdukFragmentDirections.actionNavigationProdukToNavigationDetailProduk(productId)
        findNavController().navigate(action)
    }

    private fun performSearch(searchQuery: String) {
        val action = ProdukFragmentDirections.actionNavigationProdukToNavigationSearch(searchQuery)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}