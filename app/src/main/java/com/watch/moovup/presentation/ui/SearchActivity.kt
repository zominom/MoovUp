package com.watch.moovup.presentation.ui

 import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.watch.moovup.presentation.LocationManager
import com.watch.moovup.presentation.ui.commonDesigns.SearchButton
import com.watch.moovup.presentation.ui.theme.MoovupTheme

class SearchActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = LocationManager(this@SearchActivity)

        setContent {
            SearchApp()
        }
    }

    fun search(originOptions: List<String>, destinationOptions: List<String>) {
        var intent = Intent(this@SearchActivity, LocationOptionsActivity::class.java)
        intent.putExtra("origins", originOptions.toTypedArray())
        intent.putExtra("destinations", destinationOptions.toTypedArray())
        startActivity(intent)
    }

    @Composable
    fun InputCard(label: String, text: String, onTextChange: (String) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = label)
                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.colors()
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showBackground = true)
    @Composable
    fun SearchApp() {
        var originInput by remember { mutableStateOf("") }
        var destinationInput by remember { mutableStateOf("") }

        var originOptions by remember { mutableStateOf<List<String>>(emptyList()) }
        var destinationOptions by remember { mutableStateOf<List<String>>(emptyList()) }

        var currentLocation by remember { mutableStateOf<String>("") }

        locationManager.requestLocationUpdates {
            locationManager.getAddressFromLonLat(it!!, onLocationReceived = {
                currentLocation = it[0]
                originInput = it[0]
            })
        }

        LaunchedEffect(key1 = originInput) {
            originOptions = emptyList()
            if (currentLocation != originInput) {
                locationManager.getLocationFromString(
                    originInput,
                    onLocationReceived = { locations ->
                        locations.forEach { location ->
                            locationManager.getAddressFromLonLat(location) { originOptions += it[0] }
                        }
                    })
            }
            else {
                originOptions += currentLocation
            }
        }

        LaunchedEffect(key1 = destinationInput) {
            destinationOptions = emptyList()
            locationManager.getLocationFromString(
                destinationInput,
                onLocationReceived = { locations ->
                    locations.forEach { location ->
                        locationManager.getAddressFromLonLat(location) { destinationOptions += it[0] }
                    }
                })
        }


        MoovupTheme {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                item {
                    InputCard(
                        label = "נקודת מוצא: (המיקום הנוכחי כברירת מחדל)",
                        text = originInput,
                        onTextChange = { originInput = it }
                    )
                        InputCard(
                            label = "נקודת יעד:",
                            text = destinationInput,
                            onTextChange = { destinationInput = it }
                        )
                    SearchButton(onClick = { search(originOptions, destinationOptions) })
                }
            }
        }
    }
}