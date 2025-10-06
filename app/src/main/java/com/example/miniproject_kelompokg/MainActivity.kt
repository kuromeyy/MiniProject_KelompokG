package com.example.miniproject_kelompokg

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // simpan marker ke list
    private val markers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // agar ada tombol untuk zoom in zoom out
        with(mMap.uiSettings) {
            isZoomControlsEnabled = true
        }

        // posisi pas app muncul di umn
        val umn = LatLng(-6.2574591, 106.6183484)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(umn, 15f))

        // kalo klik area kosong bs tambah marker
        mMap.setOnMapClickListener { latLng ->
            showAddMarkerDialog(latLng)
        }

        // klik marker akan memunculkan detail marker
        mMap.setOnMarkerClickListener { marker ->
            showMarkerInfoDialog(marker)
            true
        }
    }

    private fun showAddMarkerDialog(latLng: LatLng) {
        val titleInput = EditText(this).apply { hint = "Masukkan judul" }
        val descInput = EditText(this).apply { hint = "Masukkan deskripsi" }

        // layout popup
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
            addView(titleInput)
            addView(descInput)
        }

        AlertDialog.Builder(this)
            .setTitle("Tambah Marker Baru")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                val title = titleInput.text.toString()
                val desc = descInput.text.toString()

                if (title.isNotEmpty()) {
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(title)
                            .snippet(desc)
                    )
                    marker?.let { markers.add(it) }

                    Toast.makeText(
                        this,
                        "Marker '$title' berhasil ditambahkan!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Judul tidak boleh kosong!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // fungsi untuk menampilkan detail marker
    private fun showMarkerInfoDialog(marker: Marker) {

        val message = "Judul: ${marker.title}\nDeskripsi: ${marker.snippet}"

        AlertDialog.Builder(this)
            .setTitle("Detail Marker")
            .setMessage(message)
            .setPositiveButton("Tutup", null)
            .setNegativeButton("Hapus Marker") { _, _ ->
                marker.remove()
                markers.remove(marker)
                Toast.makeText(this, "Marker dihapus", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}
