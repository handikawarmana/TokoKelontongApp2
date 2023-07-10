package com.example.tokokelontongapp.ui

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tokokelontongapp.R
import com.example.tokokelontongapp.application.GroceriesApp
import com.example.tokokelontongapp.databinding.FragmentSecondBinding
import com.example.tokokelontongapp.model.Groceries
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val groceriesViewModel: GroceriesViewModel by viewModels {
        GroceriesViewModelFactory((applicationContext as GroceriesApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var groceries: Groceries? = null
    private lateinit var mMap: GoogleMap
    private var currentLatLang: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groceries = args.groceries
        //jika groceries null maka tampilan default nambah toko kelontong
        // jika groceries tidak null tampilan berubah
        if (groceries != null) {
            binding.deleteButon.visibility = View.VISIBLE
            binding.saveButon.text = "Rubah"
            binding.nameEditText.setText(groceries?.name)
            binding.addressEditText.setText(groceries?.address)
            binding.numberEditText.setText(groceries?.number)
        }

        // binding google map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()

        val name = binding.nameEditText.text
        val address = binding.addressEditText.text
        val number= binding.numberEditText.text
        binding.saveButon.setOnClickListener {
            if (name.isEmpty()) {
                Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if (address.isEmpty()) {
                Toast.makeText(context, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {

                if (groceries == null){
                    val groceries = Groceries(0, name.toString(), address.toString(), number.toString(), currentLatLang?.latitude,currentLatLang?.longitude)
                    groceriesViewModel.insert(groceries)
                } else {
                    val groceries = Groceries(groceries?.id!!, name.toString(), address.toString(), number.toString(),currentLatLang?.latitude,currentLatLang?.longitude)
                    groceriesViewModel.update(groceries)
                }
                findNavController().popBackStack() // dismiss halaman
            }
        }

        binding.deleteButon.setOnClickListener {
            groceries?.let { groceriesViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // implement drag marker


        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) { }

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatLang = LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context,currentLatLang.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {
    }

    private fun checkPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else{
            Toast.makeText(applicationContext,"Akses lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation() {
        // cek jika permissin tidak disetujui akan berhenti di kondisi if
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    var latLang = LatLng(location.latitude, location.longitude)
                    currentLatLang = latLang
                    var title = "Marker"


                    if (groceries !=null) {
                        title = groceries?.name.toString()
                        val newCurrentLocation = LatLng(groceries?.latitude!!, groceries?.longitude!!)
                        latLang = newCurrentLocation
                    }
                    val markerOption = MarkerOptions()
                        .position(latLang)
                        .title(title)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shopping_bag_32))
                    mMap.addMarker(markerOption)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang,15f))
                }
            }
    }
}