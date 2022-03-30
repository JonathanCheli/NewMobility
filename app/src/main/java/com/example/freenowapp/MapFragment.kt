package com.example.freenowapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freenowapp.adapter.Adapter
import com.example.freenowapp.adapter.ItemClickListener
import com.example.freenowapp.databinding.BottomSheetLayoutBinding
import com.example.freenowapp.databinding.FragmentMapBinding
import com.example.freenowapp.model.Poi
import com.example.freenowapp.utils.Status
import com.example.freenowapp.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@AndroidEntryPoint
class MapFragment : Fragment(), ItemClickListener {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var _binding: FragmentMapBinding
    private val binding get() = _binding


    private lateinit var vehicleListAdapter: Adapter
    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap
    private  var list: List<Poi>? = null
    private val listLocation: MutableList<LatLng> = mutableListOf()
    private var allMarkers: MutableList<Marker> = mutableListOf()
    private lateinit var locationMarker: Marker


    private var mData: List<Poi>? =null
    private var mLocation: LatLng? =null

    private lateinit var _bindingS:BottomSheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        _bindingS = BottomSheetLayoutBinding.bind(binding.root)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = binding.map

        viewLifecycleOwner.lifecycleScope.launch {
            startMap()

        }
        populateData()


        binding.viewAll.setOnClickListener {
            viewAll()
        }


    }

    private fun viewAll() {
        Log.d("TAG", "startMapList: $list")
        val builder = LatLngBounds.Builder()
        val locBounds = LatLngBounds(LatLng(53.394655, 10.09989), LatLng(53.694865, 9.75758))
        builder.include(locBounds.southwest)
        builder.include(locBounds.northeast)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))


        mainViewModel.mVehicles.observe(viewLifecycleOwner) {
            val data = it
            Log.d("TAG", "startMapNewList:$data ")
            data.data?.forEach { poi ->
                val latitude = poi.coordinate.latitude
                val longitude = poi.coordinate.longitude
                val location = LatLng(latitude, longitude)
                mLocation = location
                Log.d("TAG", "Location: $location")
                listLocation.add(location)
            }
            Log.d("TAG", "LocationArray:$listLocation ")
            listLocation.forEach { place ->
                locationMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(place)
                        .icon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(requireContext())))
                )!!
                allMarkers.add(locationMarker)

            }

        }
    }

    private fun populateData() {
        mainViewModel.getVehicles(53.694865, 9.757589,  53.394655,10.099891)
        mainViewModel.mVehicles.observe(viewLifecycleOwner) {
            it?.let { resource ->
                Log.d("TAG", "populateData: $resource")
                when (resource.status) {
                    Status.SUCCESS -> {


                        viewAll()

                        _bindingS.vehiclesRecyclerView.visibility = View.VISIBLE
                        resource.data?.let { data ->

                            mData = data
                            // mainViewModel.setVehicles(data)
                            Log.d("TAG", "populateDataList: $list")
                            vehicleListAdapter = Adapter(requireContext(), data, this)
                            _bindingS.vehiclesRecyclerView.adapter = vehicleListAdapter
                            _bindingS.vehiclesRecyclerView.layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        }


                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        Toast.makeText(context, "${resource.message}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    private suspend fun startMap() {
        mMapView.onCreate(null)
        withContext(Dispatchers.Default) {
            mMapView.onResume()
            try {
                MapsInitializer.initialize(requireContext())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            withContext(Dispatchers.Main) {
                mMapView.getMapAsync { mMap ->
                    googleMap = mMap
                    Log.d("TAG", "startMapList: $list")
                    val builder = LatLngBounds.Builder()
                    val locBounds = LatLngBounds(LatLng(53.394655, 10.09989), LatLng(53.694865, 9.75758))
                    builder.include(locBounds.southwest)
                    builder.include(locBounds.northeast)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
                    googleMap.uiSettings.isCompassEnabled = true



                }

            }
        }

    }

    override fun onCardClick(poi: Poi) {
        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

        val latitude = poi.coordinate.latitude
        val longitude = poi.coordinate.longitude
        val location = LatLng(latitude, longitude)
        try {

            val addresses: List<Address> = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            if(addresses != null) {
                val address: String =
                    addresses[0].getAddressLine(0)

                val city: String = addresses[0].locality
                val state: String = addresses[0].adminArea
                val country: String = addresses[0].countryName
                val postalCode: String = addresses[0].postalCode
                val knownName: String = addresses[0].getAddressLine(0)
                // change location bounds and move camera to exact location
                listLocation.clear()
                Log.d("TAG", "onCardClick: ${poi.fleetType}")
                //removeAllMarkers()
                Log.d("TAG", "Location: $location")
                listLocation.add(location)
                val cameraPosition = CameraPosition.Builder().target(location).zoom(15.0f).build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))



                listLocation.forEach { place ->
                    locationMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(place)
                            .title(knownName)
                            .snippet("")
                            .icon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(requireContext())))
                    )!!
                    allMarkers.add(locationMarker)


                }
            }else{
                Log.w("My Current location", "No Address returned!")

            }

        }catch (e : Exception ) {
            e.printStackTrace()
            Log.w("My Current location", "Cannot get Address!")
        }

    }

    private fun getCarBitmap(context: Context): Bitmap {
        val bitmap: Bitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.taxi_ov)
        return Bitmap.createScaledBitmap(bitmap, 100, 100, false)


    }

}