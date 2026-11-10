package com.example.lab12_maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.maps.CameraUpdateFactory
import com.example.lab12_maps.R


@Composable
fun MapScreen() {
    val context = LocalContext.current
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val locations = listOf(
        Triple(LatLng(-16.433415, -71.5442652), "José Luis Bustamante y Rivero", "Zona de referencia 1"),
        Triple(LatLng(-16.4205151, -71.4945209), "Paucarpata", "Zona de referencia 2"),
        Triple(LatLng(-16.3524187, -71.5675994), "Zamácola", "Zona de referencia 3")
    )
    val routePoints = listOf(
        LatLng(-16.4040102, -71.559611),
        LatLng(-16.386000, -71.548000),
        LatLng(-16.360000, -71.575000),
        LatLng(-16.320000, -71.640000)
    )
    var boatIcon by remember { mutableStateOf<com.google.android.gms.maps.model.BitmapDescriptor?>(null) }
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    val mapProperties = MapProperties(
        mapType = mapType,
        mapStyleOptions = if (mapType == MapType.NORMAL) {
            MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark)
        } else {
            null
        }
    )
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(-16.2520984, -71.6836503),
                12f
            ),
            durationMs = 3000
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Añadir GoogleMap al layout
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapLoaded = {
                boatIcon = BitmapDescriptorFactory.fromBitmap(
                    drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_boat)!!)
                )
            }
        ) {
            Marker(
                state = rememberMarkerState(position = arequipaLocation),
                title = "Arequipa, Perú",
                icon = boatIcon
            )
            locations.forEach { (location, title, snippet) ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = title,
                    snippet = snippet,
                    icon = boatIcon
                )
            }
            Polyline(
                points = routePoints,
                color = androidx.compose.ui.graphics.Color(0xFFFF8A65),
                width = 10f
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(onClick = { mapType = MapType.NORMAL }) { Text("Normal") }
            Button(onClick = { mapType = MapType.SATELLITE }) { Text("Satélite") }
            Button(onClick = { mapType = MapType.TERRAIN }) { Text("Terreno") }
            Button(onClick = { mapType = MapType.HYBRID }) { Text("Híbrido") }
        }
    }
}

private fun drawableToBitmap(drawable: android.graphics.drawable.Drawable): Bitmap {
    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 48
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 48
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
