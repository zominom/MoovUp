package com.watch.moovup.presentation.ui

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.watch.moovup.presentation.LocationManager
import com.watch.moovup.presentation.dbUtils.GtfsDatabase
import com.watch.moovup.presentation.model.dbModels.Stop
import com.watch.moovup.presentation.ui.theme.MoovupTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RouteActivity : ComponentActivity() {
    private lateinit var database: GtfsDatabase
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var origin = intent.getStringExtra("origin") ?: ""
        var destination = intent.getStringExtra("destination") ?: ""

        database = GtfsDatabase.getInstance(this@RouteActivity)
        locationManager = LocationManager(this@RouteActivity)

        setContent {
            RouteApp(origin, destination)
        }
    }

    private suspend fun getClosestStops(location: Location?): List<Stop> {
        return withContext(Dispatchers.IO) {
            location?.let {
                database.gtfsDao().getClosestStops(it.latitude, it.longitude)
            } ?: emptyList()
        }
    }

    @Composable
    fun RouteApp(origin: String, destination: String) {
        var closestStopsOrigin by remember { mutableStateOf(emptyList<Stop>()) }
        var closestStopsDestination by remember { mutableStateOf(emptyList<Stop>()) }

        if (origin == "")
        locationManager.requestLocationUpdates {
            CoroutineScope(Dispatchers.IO).launch {
                closestStopsOrigin = getClosestStops(it)
            }
        }
        else {
            locationManager.getLocationFromString(origin, onLocationReceived = {
                CoroutineScope(Dispatchers.IO).launch {
                    closestStopsOrigin = getClosestStops(it[0])
                }
            })
        }

        locationManager.getLocationFromString(destination, onLocationReceived = {
            CoroutineScope(Dispatchers.IO).launch {
                closestStopsDestination = getClosestStops(it[0])
            }
        })

        MoovupTheme {
        }
    }
}