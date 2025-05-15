package com.watch.moovup.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.itemsIndexed
import com.watch.moovup.presentation.ui.theme.MoovupTheme

class LocationOptionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val originOptions = intent.getStringArrayExtra("origins") ?: emptyArray()
        val destinationOptions = intent.getStringArrayExtra("destinations") ?: emptyArray()

        setContent {
            LocationOptionsApp(originOptions, destinationOptions, this)
        }
    }
}

@Composable
fun AddressList(addresses: Array<String>, visibility: Boolean, onClick: (String) -> Unit) {
    AnimatedVisibility(visible = visibility) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            scalingParams = ScalingLazyColumnDefaults.scalingParams(
                edgeScale = 0.5f,
                minTransitionArea = 0.6f,
                maxTransitionArea = 0.7f
            )
        ) {
            itemsIndexed(items = addresses) { _, address ->
                Card(
                    modifier = Modifier.padding(8.dp),
                    onClick = { onClick(address) }
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = address
                    )
                }
            }
        }
    }
}

@Composable
fun LocationOptionsApp(
    originOptions: Array<String>,
    destinationOptions: Array<String>,
    activity: LocationOptionsActivity
) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }

    var showOriginList by remember { mutableStateOf(true) }
    var showDestinationList by remember { mutableStateOf(false) }

    MoovupTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp)
        ) {
            item {
                AddressList(
                    addresses = originOptions,
                    showOriginList,
                    onClick = { selectedOrigin ->
                        origin = selectedOrigin
                        showOriginList = false
                        showDestinationList = true
                    })

                AddressList(
                    addresses = destinationOptions,
                    showDestinationList,
                    onClick = { selectedDestination ->
                        destination = selectedDestination

                        val intent = Intent(activity, RouteActivity::class.java)
                        intent.putExtra("origin", origin)
                        intent.putExtra("destination", destination)
                        activity.startActivity(intent)
                        activity.finish()
                    })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationOptionsAppPreview() {
    LocationOptionsApp(
        originOptions = arrayOf("Origin 1", "Origin 2", "Origin 3"),
        destinationOptions = arrayOf("Destination 1", "Destination 2", "Destination 3"),
        activity = LocationOptionsActivity()
    )
}