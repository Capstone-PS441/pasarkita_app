package com.rayhan.pasarkitarevision.ui.detailtoko

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rayhan.pasarkitarevision.adapter.ProductAdapter
import com.rayhan.pasarkitarevision.adapter.ProductSearchAdapter
import com.rayhan.pasarkitarevision.databinding.FragmentDetailprodukBinding
import com.rayhan.pasarkitarevision.databinding.FragmentDetailtokoBinding
import com.rayhan.pasarkitarevision.util.Constant
import com.rayhan.pasarkitarevision.viewmodel.ProductViewModel
import com.rayhan.pasarkitarevision.viewmodel.SellerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTokoFragment : Fragment(), Constant.OnItemClickListener {

    private var _binding: FragmentDetailtokoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SellerViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailtokoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnKontak.setOnClickListener {
            val phoneNumber = "+6282132909320"
            openWhatsAppChat(phoneNumber)
        }

        val sellerId = arguments?.getInt("sellerId") ?: 0
        setupViewModel(sellerId)
        setupProductRecyclerView()
        observeLiveData()
    }

    private fun setupViewModel(sellerId: Int) {
        viewModel.products.observe(viewLifecycleOwner) { productList ->
            productAdapter.productList = productList
        }
        viewModel.fetchProducts(sellerId)
    }

    private fun setupProductRecyclerView() {
        productAdapter = ProductAdapter(this)
        binding.rvProdukToko.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
    }

    private fun observeLiveData() {
        viewModel.products.observe(viewLifecycleOwner) { productList ->
            productAdapter.productList = productList
        }
    }

    private fun openWhatsAppChat(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$phoneNumber")
        startActivity(intent)
    }

    override fun onItemClick(productId: Int) {
        val action = DetailTokoFragmentDirections.actionNavigationDetailTokoToNavigationDetailProduk(productId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
