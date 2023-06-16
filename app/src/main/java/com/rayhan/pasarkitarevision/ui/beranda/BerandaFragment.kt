package com.rayhan.pasarkitarevision.ui.beranda

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.location.Address
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rayhan.pasarkitarevision.R
import com.rayhan.pasarkitarevision.adapter.CategoryAdapter
import com.rayhan.pasarkitarevision.adapter.ProductAdapter
import com.rayhan.pasarkitarevision.adapter.RecommendationAdapter
import com.rayhan.pasarkitarevision.databinding.FragmentBerandaBinding
import com.rayhan.pasarkitarevision.model.CategoryItem
import com.rayhan.pasarkitarevision.util.Constant
import com.rayhan.pasarkitarevision.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class BerandaFragment : Fragment(), Constant.OnItemClickListener {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myAddress = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.imageKeranjang.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                getLocation()
            }
        }


        setupViewModel()
        setupBanner()
        setupCategoryRecyclerView()
        setupRecommendationRecyclerView()
        observeLiveData()
    }

    private fun setupViewModel() {
        viewModel.recommendation.observe(viewLifecycleOwner) { recommendations ->
            Log.d("BerandaFragment", "Recommendations: $recommendations")
        }
        viewModel.fetchRecommendations()

//        val imageKeranjang = findViewById<ImageView>(R.id.imageKeranjang)
//        imageKeranjang.setOnClickListener {
//            val fragment = CartFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.imageKeranjang, fragment)
//                .addToBackStack(null)
//                .commit()
//        }


    }

    private fun setupBanner() {
        val imageList = ArrayList<SlideModel>()
        val imageSlider = binding.imageSlider
        imageList.add(SlideModel(R.drawable.banner))
        imageList.add(SlideModel(R.drawable.banner2))
        imageList.add(SlideModel(R.drawable.banner3))
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
        imageSlider.startSliding(3000)
    }

    private fun setupCategoryRecyclerView() {
        val categoryItems = listOf(
            CategoryItem("Sayuran", R.drawable.ic_sayuran),
            CategoryItem("Buah", R.drawable.ic_buah),
            CategoryItem("Daging", R.drawable.ic_daging),
            CategoryItem("Olahan Ikan", R.drawable.ic_olahan_ikan),
            CategoryItem("Bumbu Makanan",R.drawable.ic_bumbu_makanan),
            CategoryItem("Makanan & Minuman", R.drawable.ic_makanan_minuman)
        )

        val categoryRecyclerView = binding.rvCategory
        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val categoryAdapter = CategoryAdapter(categoryItems){ category ->
            navigateToCategoryFragment(category)
        }
        categoryRecyclerView.adapter = categoryAdapter
    }

    private fun navigateToCategoryFragment(category: CategoryItem) {
        val action = BerandaFragmentDirections.actionNavigationBerandaToNavigationCategory(category.name)
        findNavController().navigate(action)
    }

    private fun setupRecommendationRecyclerView() {
        recommendationAdapter = RecommendationAdapter(this)
        binding.rvRekomendasi.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendationAdapter
        }
    }

    private fun observeLiveData() {
        viewModel.recommendation.observe(requireActivity()) {
            recommendationAdapter.productList = it
        }
    }

    override fun onItemClick(productId: Int) {
        val action = BerandaFragmentDirections.actionNavigationBerandaToNavigationDetailProduk(productId)
        findNavController().navigate(action)
    }

    private fun performSearch(searchQuery: String) {
        val action = BerandaFragmentDirections.actionNavigationBerandaToNavigationSearch(searchQuery)
        findNavController().navigate(action)
    }

    private fun getLocation(){
//
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getCityName(location.latitude, location.longitude)
                binding.tvalamat.text = myAddress
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private fun getCityName(lat:Double, long:Double){
        try{
            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: List<Address>? = geoCoder.getFromLocation(lat, long, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                myAddress = address.getAddressLine(0)
            }
        }catch (e : Exception){
            Toast.makeText(requireContext(), "loading city", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}