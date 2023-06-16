package com.rayhan.pasarkitarevision.ui.keranjang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rayhan.pasarkitarevision.R
import com.rayhan.pasarkitarevision.adapter.CartItemAdapter
import com.rayhan.pasarkitarevision.databinding.FragmentKeranjangBinding
import com.rayhan.pasarkitarevision.model.CartItem
import com.rayhan.pasarkitarevision.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentKeranjangBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartItemAdapter: CartItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeCartItems()
        setupButtons()
    }

    private fun setupRecyclerView() {
        cartItemAdapter = CartItemAdapter { cartItem ->
            // Handle item click if needed
        }
        binding.rvKeranjang.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartItemAdapter
        }
    }

    private fun observeCartItems() {
        viewModel.getAllCartItems().observe(viewLifecycleOwner) { cartItems ->
            cartItemAdapter.submitList(cartItems)
            updateTotalPrice(cartItems)
        }
    }

    private fun updateTotalPrice(cartItems: List<CartItem>?) {
        val totalPrice = cartItems?.sumBy { it.price } ?: 0
        binding.tvTotalPrice.text = formatPrice(totalPrice)
    }

    private fun setupButtons() {
        binding.btnCheckout.setOnClickListener {
            val cartItems = viewModel.getAllCartItems().value
            if (cartItems.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Keranjang kosong", Toast.LENGTH_SHORT).show()
            } else {
                cartItems.forEach { item ->
                    viewModel.removeFromCart(item)
                }
                Toast.makeText(requireContext(), "Checkout", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_navigation_keranjang_to_navigation_checkout)
            }
        }
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
