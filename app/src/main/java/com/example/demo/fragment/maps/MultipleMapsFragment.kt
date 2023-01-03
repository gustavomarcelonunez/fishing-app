package com.example.demo.fragment.maps

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.demo.R
import com.example.demo.databinding.FragmentMultipleMapsBinding
import com.example.demo.viewModel.ReportViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MultipleMapsFragment : Fragment() {

    private var _binding: FragmentMultipleMapsBinding? = null
    private val binding get() = _binding!!
    private val reportViewModel: ReportViewModel by navGraphViewModels(R.id.app_navigation)

    private var marker: Marker? = null
    private var smallMarker: Bitmap? = null


    private val callback = OnMapReadyCallback { googleMap ->

        val height = 120
        val width = 120
        val bitmapdraw = resources.getDrawable(R.drawable.map_icon) as BitmapDrawable
        val b = bitmapdraw.bitmap
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

        val reportList = reportViewModel.allReports.value!!

        for (report in reportList) {
            val pos = LatLng(report.latitude!!, report.longitude!!)
            val snippet = String.format(
                Locale.getDefault(),
                "Tipo de pesca: %1$.15s, Fecha: %2$.15s",
                report.fishing_type,
                report.date
            )
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(pos)
                    .title(report.title)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker!!))
            )
        }
        val pos = LatLng(-42.78469053756446, -65.00895665709373)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 5f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentMultipleMapsBinding.inflate(inflater, container, false)
        val view = binding.root
        _binding!!.goBackActionButton.setOnClickListener { goBack() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun goBack() {
        findNavController().popBackStack()
    }
}