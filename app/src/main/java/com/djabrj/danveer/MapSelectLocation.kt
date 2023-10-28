package com.djabrj.danveer

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapSelectLocation : AppCompatActivity() {
    private lateinit var map: MapView
    private lateinit var mapController: IMapController
    private var selectedLocation = GeoPoint(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_select_location)

        //save button
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            saveLocation()
        }

        askPermission()

        val ctx: Context = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        mapController = map.controller
        mapController.setZoom(19.toDouble())
        val startPoint = GeoPoint(18.5845451, 73.735355)
        map.setMultiTouchControls(true);

        getCurrentLocation()

        val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        mLocationOverlay.enableMyLocation()
        map.getOverlays().add(mLocationOverlay)



        // Initialize a MapEventsReceiver for long-press events
        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                // Handle single tap if needed

                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                // Handle long-press to select location
                // You can store the selected location (p) and use it in your app
                println("single")
                println(p.latitude)
                println(p.longitude)
                selectedLocation = GeoPoint(p.latitude,p.longitude)
                updateSelectedMarker()
                return true
            }
        })
        // Add the MapEventsOverlay to the map
        map.overlays.add(0, mapEventsOverlay)


    }

    private fun getCurrentLocation(){
        // Create a location provider
        val locationProvider = GpsMyLocationProvider(applicationContext)
        locationProvider.startLocationProvider { location, _ ->
            // This callback will provide the current location
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                println(latitude)
                println(longitude)
                val geoPoint = GeoPoint(latitude, longitude)

                //set map location

                mapController.setCenter(geoPoint)


            }
        }
    }

    private fun updateSelectedMarker(){
        val startMarker = Marker(map)
        startMarker.position = selectedLocation
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.removeAt(1)
        map.overlays.add(1, startMarker)

    }

    private fun askPermission(){
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val requestCode = 123

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                this,
                "Allow location first.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun saveLocation(){
        val loc0 = GeoPoint(0, 0)
        if(selectedLocation ==  loc0){
            Toast.makeText(
                this,
                "Select location first.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            //update location data to firebase
        updateData(selectedLocation.latitude.toString(),selectedLocation.longitude.toString())

        }
    }

    fun updateData(latitude: String, longitude: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val donorProfileRef = db.collection("users")

        // Query the collection to find the document with the matching email
        val query = donorProfileRef.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                // Check if a document with the email was found
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0] // Assuming there's only one matching document
                    val documentId = document.id

                    // Create a data map with the updated values
                    val updatedData = hashMapOf(
                        "latitude" to latitude,
                        "longitude" to longitude
                    )

                    // Update the document in Firestore
                    donorProfileRef.document(documentId)
                        .update(updatedData as Map<String, Any>)
                        .addOnSuccessListener {
                            // Update was successful
                            // Handle success, if needed
                            Toast.makeText(
                                this,
                                "Location Updated.",
                                Toast.LENGTH_SHORT
                            ).show()
                            println("updated")
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // Update failed
                            // Handle the failure and show an error message or log the error, if needed
                            println("error")
                            Toast.makeText(
                                this,
                                "An Error Occured.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                } else {
                    // No document with the email was found
                    // Handle this case, e.g., display an error message
                    println("email not found")

                }
            }
            .addOnFailureListener { e ->
                // Query failed
                // Handle the query failure, e.g., show an error message or log the error
                println("Query failed")

            }
    }

}