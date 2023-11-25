package com.djabrj.danveer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


class MapShowLocation : AppCompatActivity() {
    private lateinit var map: MapView
    private lateinit var mapController: IMapController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_show_location)

        val latitude = intent.getStringExtra("latitude")?.toDouble() ?: 0.toDouble()
        val longitude = intent.getStringExtra("longitude")?.toDouble() ?: 0.toDouble()

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")

            println(gmmIntentUri)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")  //set google maps as default
            startActivity(mapIntent)
        }

        //show location on map
        val ctx: Context = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //take map to location
        mapController = map.controller
        mapController.setZoom(19.toDouble())
        val startPoint = GeoPoint(latitude, longitude)
        map.setMultiTouchControls(true);
        mapController.setCenter(startPoint)

        //show marker
        val startMarker = Marker(map)
        startMarker.position = startPoint
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(startMarker)

    }
}