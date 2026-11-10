package com.example.lab12_maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.lab12_maps.R


@Composable
fun MapScreen() {
    val context = LocalContext.current
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    var boatIcon by remember { mutableStateOf<com.google.android.gms.maps.model.BitmapDescriptor?>(null) }
    val mapProperties = remember {
        MapProperties(mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
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
