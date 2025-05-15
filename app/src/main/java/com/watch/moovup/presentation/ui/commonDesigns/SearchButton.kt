package com.watch.moovup.presentation.ui.commonDesigns

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import com.watch.moovup.R

@Composable
fun SearchButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth()
                .padding(top = 5.dp, bottom = 12.dp),

            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),

            onClick = onClick,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                )
            },
        )
    }
}