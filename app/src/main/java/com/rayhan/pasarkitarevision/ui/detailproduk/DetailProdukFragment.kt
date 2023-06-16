package com.rayhan.pasarkitarevision.ui.detailproduk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rayhan.pasarkitarevision.adapter.ProductAdapter
import com.rayhan.pasarkitarevision.data.repository.CartRepository
import com.rayhan.pasarkitarevision.databinding.FragmentDetailprodukBinding
import com.rayhan.pasarkitarevision.model.CartItem
import com.rayhan.pasarkitarevision.model.ProductItem
import com.rayhan.pasarkitarevision.ui.detailproduk.DetailProdukFragmentDirections
import com.rayhan.pasarkitarevision.util.Constant
import com.rayhan.pasarkitarevision.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailProdukFragment : Fragment(), Constant.OnItemClickListener {

    private var _binding: FragmentDetailprodukBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var otherProductAdapter: ProductAdapter

    @Inject
    lateinit var cartRepository: CartRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailprodukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDetail()
        setupViewModel()
        setupProductRecyclerView()
        observeLiveData()
        setupButtons()
    }

    private fun setupDetail() {
        val productId = arguments?.getInt("productId") ?: 0
        val sellerId = arguments?.getInt("sellerId") ?: 0

        viewModel.product.observe(viewLifecycleOwner) { product ->
            binding.tvProductName.text = product.name
            binding.tvCategory.text = "/ ${product.satuan} "
            binding.tvPrice.text = formatPrice(product.price)

            Glide.with(requireContext())
                .load(product.image)
                .into(binding.ivProduct)

            binding.tvNamaToko.text = product.toko
            binding.tvAlamatToko.text = product.location

            binding.ivToko.setOnClickListener {
                val sellerId = viewModel.product.value?.sellerId ?: 0
                navigateToDetailTokoFragment(sellerId)
            }

            binding.tvToko.setOnClickListener {
                val sellerId = viewModel.product.value?.sellerId ?: 0
                navigateToDetailTokoFragment(sellerId)
            }

            binding.btnKunjungiToko.setOnClickListener {
                val sellerId = viewModel.product.value?.sellerId ?: 0
                navigateToDetailTokoFragment(sellerId)
            }
        }
        viewModel.fetchProductById(productId)
    }

    private fun setupViewModel() {
        val productId = arguments?.getInt("productId") ?: 0

        viewModel.products.observe(viewLifecycleOwner) { products ->
            val filteredProducts = products.filter { it.id != productId }
        }
        viewModel.fetchProducts(productId)
    }

    private fun setupProductRecyclerView() {
        otherProductAdapter = ProductAdapter(this)
        binding.rvProdukLainnya.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = otherProductAdapter
        }
    }

    private fun observeLiveData() {
        viewModel.products.observe(requireActivity()) {
            otherProductAdapter.productList = it
        }
    }

    private fun setupButtons() {
        binding.btnTambahKeranjang.setOnClickListener {
            val productId = arguments?.getInt("productId") ?: 0
            viewModel.product.value?.let { product ->
                addToCart(product)
            }
        }
    }

    private fun addToCart(product: ProductItem) {
        val cartItem = CartItem(product.image, product.name, product.satuan, product.price)
        cartRepository.addToCart(cartItem)
        Toast.makeText(requireContext(), "Product added to cart", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(productId: Int) {
        val action = DetailProdukFragmentDirections.actionNavigationDetailToNavigationDetailProduk(productId)
        findNavController().navigate(action)
    }

    private fun navigateToDetailTokoFragment(sellerId: Int) {
        val action = DetailProdukFragmentDirections.actionNavigationDetailToNavigationDetailToko(sellerId)
        findNavController().navigate(action)
    }

    private fun formatPrice(price: Int): String {
        val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0
        return formatter.format(price.toLong()).replace(",00", "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
