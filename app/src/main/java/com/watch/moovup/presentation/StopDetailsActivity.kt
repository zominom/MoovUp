package com.watch.moovup.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.watch.moovup.R
import com.watch.moovup.presentation.api.RetrofitClient
import com.watch.moovup.presentation.dbUtils.GtfsDatabase
import com.watch.moovup.presentation.model.apiModels.Quote
import com.watch.moovup.presentation.model.dbModels.Route
import com.watch.moovup.presentation.theme.MoovupTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.watch.moovup.presentation.MainActivity
import com.watch.moovup.presentation.Utils.approximateDistanceInMeters
import com.watch.moovup.presentation.Utils.formatTime
import com.watch.moovup.presentation.Utils.getMinutesDifference
import com.watch.moovup.presentation.model.apiModels.MonitoredVehicleJourney
import kotlinx.coroutines.delay

class StopDetailsActivity : ComponentActivity() {

    private lateinit var database: GtfsDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = GtfsDatabase.getInstance(this@StopDetailsActivity)

        val stopId = intent.getStringExtra("stopId") ?: ""
        val stopName = intent.getStringExtra("stopName") ?: ""

        setContent {
            StopDetailsScreen(stopId, stopName, onBack = { finish() })
        }
    }
    @Composable
    fun StopDetailsScreen(stopId: String, stopName: String, onBack: () -> Unit) {
        var busArrivals by remember { mutableStateOf<List<MonitoredVehicleJourney>>(emptyList()) }
        var apiError by remember { mutableStateOf<String?>(null) }

        BackHandler { onBack() }

        LaunchedEffect(key1 = stopId) {
            val apiService = RetrofitClient.create()
            val call: Call<Quote> = apiService.getQuote("", stopId)
            call.enqueue(object : Callback<Quote> {
                override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                    if (response.isSuccessful) {
                        val quote: Quote? = response.body()
                        quote?.let {
                            busArrivals = extractBusArrivals(it)
                            apiError = null
                        }
                    } else {
                        apiError = "Error while fetching quote"
                    }
                }

                override fun onFailure(call: Call<Quote>, t: Throwable) {
                    apiError = "Error fetching quote"
                }
            })
        }
        MoovupTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back",
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stopName,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                }
                if (apiError != null) {
                    Text(text = "Error: $apiError", color = Color.Red)
                } else if (busArrivals.isEmpty()) {
                    Text(text = "Loading...", color = Color.White)
                } else {
                    ScalingLazyColumn(
                        scalingParams = ScalingLazyColumnDefaults.scalingParams(
                            edgeScale = 0.5f,
                            minTransitionArea = 0.6f,
                            maxTransitionArea = 0.7f
                        )
                    )
                    {
                        itemsIndexed(items = busArrivals.take(20)) { index, busArrival ->
                            BusArrivalCard(busArrival = busArrival)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BusArrivalCard(busArrival: MonitoredVehicleJourney) {
        var busLine by remember { mutableStateOf<Route?>(null) }

        LaunchedEffect(key1 = busArrival.lineRef != null) {
            if (busArrival.lineRef != null)
                busLine = database.gtfsDao().findRouteById(busArrival.lineRef)
        }

        Card(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        AnimatedSemiCircles(modifier = Modifier.size(20.dp))
                        Text(
                            text = busArrival.monitoredCall?.expectedArrivalTime?.let {
                                "${getMinutesDifference(formatTime(it))}"
                            } ?: "N/A",
                            fontWeight = FontWeight.Bold,
                            color = if (busArrival.vehicleLocation != null) Color.Green else Color.White
                        )
                    }

                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = busLine?.routeShortName ?: "N/A",
                        fontWeight = FontWeight.Bold,
                        color = Color.Yellow
                    )
                    Text(
                        text = busLine?.routeLongName?.replace("<->", "\n") ?: "N/A",
                        fontSize = 10.sp,
                        color = Color.Yellow
                    )
                }
            }
        }
    }

    fun extractBusArrivals(quote: Quote): List<MonitoredVehicleJourney> {
        val arrivals = mutableListOf<MonitoredVehicleJourney>()
        quote.siri?.serviceDelivery?.stopMonitoringDelivery?.forEach { delivery ->
            delivery.monitoredStopVisit?.forEach { visit ->
                visit.monitoredVehicleJourney?.let { journey ->
                    arrivals.add(journey)
                }
            }
        }
        return arrivals
    }

    @Composable
    fun AnimatedSemiCircles(
        modifier: Modifier = Modifier,
        color1: Color = Color.Green,
        color2: Color = Color.Green,
        color3: Color = Color.Green,
        initialDelay: Long = 0L,
        durationMillis: Int = 1000
    ) {
        val animationDuration = durationMillis
        // Scale values for each semi-circle
        val scale1 = remember { Animatable(0.8f) } // Smallest
        val scale2 = remember { Animatable(0.8f) } // Middle
        val scale3 = remember { Animatable(0.8f) } // Largest

        LaunchedEffect(key1 = Unit) {
            // First circle animation
            delay(initialDelay)
            scale1.animateTo(
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        LaunchedEffect(key1 = Unit) {
            // Second circle animation
            delay(initialDelay + animationDuration/3)
            scale2.animateTo(
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        LaunchedEffect(key1 = Unit) {
            // third circle animation
            delay(initialDelay + (animationDuration*2)/3)
            scale3.animateTo(
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }

        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            // Draw the three semi-circles
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Calculate semi-circle size and position based on available space
                val canvasWidth = size.width
                val canvasHeight = size.height
                val radius = (canvasWidth/2)
                val center = Offset(canvasWidth / 2, canvasHeight/2)
                val strokeWidth = radius/20
                val sweepAngle = 180f // Semi-circle
                // Draw the three semi-circles
                drawArc(
                    color = color3, //largest circle
                    startAngle = 180f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - (radius*scale3.value) , center.y - (radius*scale3.value)),
                    size = Size(radius * scale3.value * 2, radius * scale3.value * 2),
                    style = Stroke(width = strokeWidth)
                )
                drawArc(
                    color = color2, //middle circle
                    startAngle = 180f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - (radius * scale2.value * 0.8f), center.y - (radius * scale2.value * 0.8f)),
                    size = Size(radius * scale2.value * 2 * 0.8f, radius * scale2.value * 2 * 0.8f),
                    style = Stroke(width = strokeWidth)
                )

                drawArc(
                    color = color1, //smallest circle
                    startAngle = 180f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - (radius * scale1.value * 0.6f), center.y - (radius * scale1.value * 0.6f)),
                    size = Size(radius * scale1.value * 2 * 0.6f, radius * scale1.value * 2 * 0.6f),
                    style = Stroke(width = strokeWidth)
                )
            }
        }
    }
}