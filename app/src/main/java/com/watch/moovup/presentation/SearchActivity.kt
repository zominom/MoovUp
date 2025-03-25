package com.watch.moovup.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.watch.moovup.presentation.ui.theme.MoovupTheme

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchApp() {
    MoovupTheme {
        Card {

        }
        Card {

        }
        Button(
            onClick = TODO(),
            modifier = TODO(),
            enabled = TODO(),
            shape = TODO(),
            colors = TODO(),
            elevation = TODO(),
            border = TODO(),
            contentPadding = TODO(),
            interactionSource = TODO()
        ) { }
    }
}