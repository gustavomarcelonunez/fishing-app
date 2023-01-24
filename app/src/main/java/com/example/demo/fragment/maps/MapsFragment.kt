package com.example.demo.fragment.maps

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.demo.R
import com.example.demo.databinding.FragmentMapsBinding
import com.example.demo.viewModel.ReportViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.util.*

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val args: MapsFragmentArgs by navArgs()
    private lateinit var model: ReportViewModel
    private var marker: Marker? = null
    private var smallMarker: Bitmap? = null

    private val db = FirebaseFirestore.getInstance()

    private val callback = OnMapReadyCallback { googleMap ->

        val height = 120
        val width = 120
        val bitmapdraw = resources.getDrawable(R.drawable.map_icon) as BitmapDrawable
        val b = bitmapdraw.bitmap
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

        if (args.currentReport.latitude != null && args.currentReport.longitude != null) {
            val pos = LatLng(args.currentReport.latitude!!, args.currentReport.longitude!!)
            val snippet = String.format(
                Locale.getDefault(),
                "Tipo: %1$.15s - Especie: %2$.15s - Fecha: %3$.15s",
                args.currentReport.fishing_type,
                args.currentReport.specie,
                args.currentReport.date
            )
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(pos)
                    .title("Título: " + args.currentReport.title)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker!!))
            )
            marker!!.showInfoWindow()
            if (findNavController().previousBackStackEntry?.destination?.displayName!! == "com.example.demo:id/report_update_fragment") {
                _binding!!.sendReportActionButton.show()
                setMapLongClick(googleMap)
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker!!.position, 12F))
        } else {
            _binding!!.sendReportActionButton.show()
            val pos = LatLng(-42.78469053756446, -65.00895665709373)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 12F))
            setMapLongClick(googleMap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val view = binding.root
        model = ViewModelProvider(this)[ReportViewModel::class.java]

        _binding!!.goBackActionButton.setOnClickListener { goBack() }
        if (findNavController().previousBackStackEntry?.destination?.displayName!! == "com.example.demo:id/report_update_fragment") {
            _binding!!.sendReportActionButton.setOnClickListener { updateReport() }
        } else {
            _binding!!.sendReportActionButton.setOnClickListener { sendReport() }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setMapLongClick(map: GoogleMap) {

        map.setOnMapLongClickListener { latLng ->
            // Snippet --> texto adicional que se muestra debajo del titulo.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )

            if (marker == null) {
                marker = map.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(args.currentReport.title)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker!!))
                )
                args.currentReport.latitude = latLng.latitude
                args.currentReport.longitude = latLng.longitude
                marker!!.showInfoWindow()
            } else {
                marker!!.hideInfoWindow()
                marker!!.position = latLng
                marker!!.snippet = snippet
                args.currentReport.latitude = latLng.latitude
                args.currentReport.longitude = latLng.longitude
                marker!!.showInfoWindow()
            }
        }
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    private fun sendReport() {
        if (checkCoords()) {

            db.collection("reports").document().set(
                hashMapOf(
                    "title" to args.currentReport.title,
                    "fishing_type" to args.currentReport.fishing_type,
                    "specie" to args.currentReport.specie,
                    "date" to args.currentReport.date,
                    "photo_path" to args.currentReport.photo_path,
                    "latitude" to args.currentReport.latitude,
                    "longitude" to args.currentReport.longitude
                )
            )
            model.insert(args.currentReport)
            Toast.makeText(activity, "Reporte agregado correctamente", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.my_reports_fragment)
        }
    }

    private fun checkCoords(): Boolean {
        if (args.currentReport.latitude == null && args.currentReport.longitude == null) {
            Toast.makeText(activity, "Seleccione una ubicación en el mapa", Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    private fun updateReport() {

        // Los reportes NO se actualizan en la nube. Los id's no coinciden, ya que en la bd local se guardan
        // con un id numerico autoincrementaado, y en firebase se genera un id random.
        // Tampoco se actualizan en la bd local si se hace desde la nube.
        // Esto habría que verlo en el futuro si el proyecto prospera

        db.collection("reports").document(args.currentReport.id.toString()).update(
                "title", args.currentReport.title,
                "fishing_type", args.currentReport.fishing_type,
                "specie", args.currentReport.specie,
                "date", args.currentReport.date,
                "photo_path", args.currentReport.photo_path,
                "latitude", args.currentReport.latitude,
                "longitude", args.currentReport.longitude
        )

        model.updateReport(args.currentReport)
        Toast.makeText(activity, "Reporte editado correctamente", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.my_reports_fragment)
    }
}
