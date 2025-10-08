package com.example.miniproject_kelompokg

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val markers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        with(mMap.uiSettings) {
            isZoomControlsEnabled = true
        }

        val umn = LatLng(-6.2574591, 106.6183484)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(umn, 15f))

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
        val titleInput = EditText(requireContext()).apply {
            hint = "Masukkan judul"
        }
        val descInput = EditText(requireContext()).apply {
            hint = "Masukkan deskripsi"
        }

        // layout popup
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
            addView(titleInput)
            addView(descInput)
        }

        AlertDialog.Builder(requireContext())
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
                        requireContext(),
                        "Marker '$title' berhasil ditambahkan!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
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

        AlertDialog.Builder(requireContext())
            .setTitle("Detail Marker")
            .setMessage(message)
            .setPositiveButton("Tutup", null)
            .setNegativeButton("Hapus Marker") { _, _ ->
                marker.remove()
                markers.remove(marker)
                Toast.makeText(requireContext(), "Marker dihapus", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}