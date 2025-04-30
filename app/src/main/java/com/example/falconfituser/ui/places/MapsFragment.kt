package com.example.falconfituser.ui.places

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.falconfituser.R
import com.example.falconfituser.databinding.FragmentMapsBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.falconfituser.data.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.launch
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                                           Manifest.permission.ACCESS_COARSE_LOCATION)
@AndroidEntryPoint
class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding
    private val viewModel: PlacesListViewModel by viewModels()
    private lateinit var map: GoogleMap

    val contract = ActivityResultContracts.RequestMultiplePermissions()

    val launcher = registerForActivityResult(contract){
        permissions ->
        var granted = true
        permissions.entries.forEach{
            permission ->
                if(permission.key in PERMISSIONS_REQUIRED && !permission.value){
                    granted = false
                }
        }

        if(!granted){
            Toast.makeText(requireContext(), "No tiene permisos de localizacion", Toast.LENGTH_LONG).show()
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        printPlaces()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(
            inflater,
            container,
            false
        )
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun printPlaces(){
        lifecycleScope.launch {
            if (hasLocationPermissions(requireContext())){
                try {
                    val places = viewModel.loadPlaces()
                    if (places.isNotEmpty()){
                        for (place in places){
                            addMarker(place)
                        }
                    }

                } catch (e: HttpException) {
                    Log.e("MapsFragment", "Error HTTP: ${e.message}")
                    Toast.makeText(binding.root.context, "Error al cargar sesiones", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("MapsFragment", "Error inesperado: ${e.message}")
                    Toast.makeText(binding.root.context, "Error inesperado", Toast.LENGTH_SHORT).show()
                }
            }else{
                launcher.launch(PERMISSIONS_REQUIRED)
            }
        }
    }

    private fun addMarker(place: Places) {
        val latitude = place.latitud
        val longitude = place.longitud
        val place = place.title

        if (latitude != null && longitude != null) {
            val location = LatLng(latitude, longitude)
            map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(place)
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }

    // Comprobar si tenemos permiso
    private fun hasLocationPermissions(context: Context):Boolean{
        return PERMISSIONS_REQUIRED.all{
                permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}
