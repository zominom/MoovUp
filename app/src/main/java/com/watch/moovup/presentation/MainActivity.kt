package com.watch.moovup.presentation

import android.Manifest
import com.watch.moovup.R
import android.R.style.Theme_DeviceDefault
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.watch.moovup.presentation.Utils.approximateDistanceInMeters
import com.watch.moovup.presentation.theme.MoovupTheme
import java.util.concurrent.TimeUnit
import com.watch.moovup.presentation.dbUtils.GtfsDatabase
import com.watch.moovup.presentation.model.dbModels.Stop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var database: GtfsDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(Theme_DeviceDefault)

        database = GtfsDatabase.getInstance(this@MainActivity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(10))
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis (TimeUnit.SECONDS.toMillis(5))
            .setMaxUpdateDelayMillis(TimeUnit.SECONDS.toMillis(15))
            .build()

        setContent {
            MainApp()
        }
    }

    private fun requestLocationUpdates(onLocationReceived: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    onLocationReceived(location)
                }
        }else {
            onLocationReceived(null)
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
    fun SearchButton() {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth()
                    .padding(top = 5.dp, bottom = 12.dp),

                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),

                onClick = {
                    var intent = Intent(this@MainActivity, SearchActivity::class.java)
                    startActivity(intent)
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "call",
                    )
                },
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StopList(
        userLocation: Location?,
        closestStops: List<Stop>
    ) {
        ScalingLazyColumn(
            scalingParams = ScalingLazyColumnDefaults.scalingParams(
                edgeScale = 0.5f,
                minTransitionArea = 0.6f,
                maxTransitionArea = 0.7f
            )
        ) {
            itemsIndexed(items = closestStops.take(20)) { index, stop ->

                Card(
                    onClick = {
                        val intent = Intent(this@MainActivity, StopDetailsActivity::class.java)
                        intent.putExtra("stopId", "${stop.stopCode}")
                        intent.putExtra("stopName", stop.stopName)
                        startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = Color.White,
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.stop),
                            contentDescription = "Stop",
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Column(
                            modifier = Modifier.padding(start = 9.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = stop.stopName)
                            Text(
                                text = "מרחק: ${
                                    approximateDistanceInMeters(
                                        userLocation?.latitude ?: stop.stopLat,
                                        userLocation?.longitude ?: stop.stopLon,
                                        stop.stopLat,
                                        stop.stopLon
                                    )
                                } מ'"
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun MainApp() {
        var hasLocationPermission by remember { mutableStateOf(false) }
        var userLocation by remember { mutableStateOf<Location?>(null) }
        var closestStops by remember { mutableStateOf<List<Stop>> (emptyList()) }

        val pullToRefreshState = rememberPullToRefreshState()
        var isRefreshing by remember { mutableStateOf(false) }

        var exit by remember { mutableStateOf(false) }
        val context = LocalContext.current

        LaunchedEffect(key1 = exit) {
            if (exit) {
                delay(2000)
                exit = false
            }
        }

        BackHandler(enabled = true) {
            if (exit) {
                finish()
            } else {
                exit = true
                isRefreshing = true
                Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
            }
        }


        val locationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                hasLocationPermission = isGranted
            }
        )

        LaunchedEffect(key1 = true) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                hasLocationPermission = true
            }
        }

        LaunchedEffect(key1 = hasLocationPermission) {
            if (hasLocationPermission) {
                requestLocationUpdates {
                    userLocation = it
                    CoroutineScope(Dispatchers.IO).launch {
                        closestStops = getClosestStops(it)
                    }
                }
            }
        }

        LaunchedEffect(key1 = isRefreshing) {
            if (isRefreshing) {
                if (hasLocationPermission) {
                    requestLocationUpdates {
                        CoroutineScope(Dispatchers.IO).launch {
                            closestStops = getClosestStops(it)
                        }
                    }
                }
                delay(1000)
                isRefreshing = false
            }
        }

        MoovupTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                SearchButton()
                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = { isRefreshing = true },
                ) {
                    StopList(userLocation, closestStops)
                }
            }
        }
    }
}