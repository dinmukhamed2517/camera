package kz.just_code.mls

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationTextView: TextView
    private lateinit var refreshButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        locationTextView = view.findViewById(R.id.locationTextView)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        refreshButton = view.findViewById(R.id.refreshButton)


        refreshButton.setOnClickListener {
            requestLocation()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        if (checkLocationPermission()) {
            requestLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION_PERMISSION
        )
    }

    private val REQUEST_LOCATION_PERMISSION = 1



    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val coordinates = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
                    locationTextView.text = coordinates
                } else {
                    locationTextView.text = "Location not available"
                }
            }
            .addOnFailureListener { e ->
                locationTextView.text = "Error: ${e.message}"
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation()
            } else {
                locationTextView.text = "Location permission denied"
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}