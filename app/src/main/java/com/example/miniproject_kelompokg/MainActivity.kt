package com.example.miniproject_kelompokg  // sesuaikan dengan package project kamu

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // Data event (hardcoded)
    private val eventPlaces = listOf(
        EventPlace("Lapangan Badminton", "Main jam 7 malam", -6.2574591, 106.6183484),
        EventPlace("Kampus UMN", "Belajar bareng jam 10 pagi", -6.256302, 106.617534),
        EventPlace("Warung Makan", "Makan siang jam 12", -6.254611, 106.622085),
        EventPlace("Perpustakaan", "Baca buku jam 3 sore", -6.256718, 106.618209)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Tambahkan marker dari event list
        for (place in eventPlaces) {
            val latLng = LatLng(place.lat, place.lng)
            mMap.addMarker(
                MarkerOptions().position(latLng).title(place.name).snippet(place.description)
            )
        }

        // Fokus kamera ke UMN
        val umn = LatLng(-6.2574591, 106.6183484)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(umn, 15f))

        // Event klik marker
        mMap.setOnMarkerClickListener { marker ->
            Toast.makeText(
                this,
                "📌 ${marker.title}: ${marker.snippet}",
                Toast.LENGTH_SHORT
            ).show()
            false // biar info window bawaan juga muncul
        }
    }
}
