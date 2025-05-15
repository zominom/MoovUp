package com.watch.moovup.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.IOException
import java.util.concurrent.TimeUnit

class LocationManager {
    private var _fusedLocationClient: FusedLocationProviderClient
    private var _locationRequest: LocationRequest
    private var _context: Context

    constructor(ctx: Context) {
        _context = ctx
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
        _locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(10))
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis (TimeUnit.SECONDS.toMillis(5))
            .setMaxUpdateDelayMillis(TimeUnit.SECONDS.toMillis(15))
            .build()
    }

    fun requestLocationUpdates(onLocationReceived: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                _context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            _fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    onLocationReceived(location)
                }
        }
        else {
            onLocationReceived(null)
        }
    }

    fun getAddressFromLonLat(
        location: Location,
        count: Int = 1,
        onLocationReceived: (List<String>) -> Unit
    ) {
        val geocoder = Geocoder(_context)
        try {
            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                count,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address>) {
                        if (addresses.isNotEmpty()) {
                            val addressList = mutableListOf<String>()

                            addresses.forEach { address ->
                                addressList += address.getAddressLine(0)
                            }
                            onLocationReceived(addressList)
                        } else {
                            onLocationReceived(emptyList())
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        println("Geocoder error: $errorMessage")
                        onLocationReceived(emptyList())
                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
            onLocationReceived(emptyList())
        }
    }

    fun getLocationFromString(
        address: String,
        count: Int = 1,
        onLocationReceived: (List<Location>) -> Unit
    ) {
        val geocoder = Geocoder(_context)
        try {
            geocoder.getFromLocationName(
                address,
                count,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address>) {
                        if (addresses.isNotEmpty()) {
                            var locationList = mutableListOf<Location>()

                            addresses.forEach { address ->
                                var location = Location("")
                                location.latitude = address.latitude
                                location.longitude = address.longitude

                                locationList += location
                            }
                            onLocationReceived(locationList)
                        } else {
                            onLocationReceived(emptyList())
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        println("Geocoder error: $errorMessage")
                        onLocationReceived(emptyList())
                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
            onLocationReceived(emptyList())
        }
    }
}