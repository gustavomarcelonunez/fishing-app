package com.example.demo.fragment.detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.demo.R
import com.example.demo.databinding.FragmentReportDetailBinding
import java.io.File

class ReportDetailFragment : Fragment() {

    private var _binding: FragmentReportDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ReportDetailFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportDetailBinding.inflate(inflater, container, false)

        _binding!!.titleTextView.text = args.currentReport.title
        _binding!!.fishingTypeTextView.text = args.currentReport.fishing_type
        _binding!!.specieTextView.text = args.currentReport.specie
        _binding!!.dateTextView.text = args.currentReport.date

        val file = File(args.currentReport.photo_path)
        if (file.exists()) {
            val imageBitmap: Bitmap? = BitmapFactory.decodeFile(args.currentReport.photo_path)
            rotateImage(imageBitmap!!)
        }

        _binding!!.doneButton.setOnClickListener { goBack() }
        _binding!!.updateButton.setOnClickListener { updateReport() }
        _binding!!.mapButton.setOnClickListener { goMap() }

        return binding.root
    }

    private fun goMap() {
        val action = ReportDetailFragmentDirections.goToMapsFragmentFromReportDetailFragment(args.currentReport)
        findNavController().navigate(action)
    }

    private fun goBack() {
        findNavController().navigate(R.id.goToMyReportsFromReportDetailAction)
    }

    private fun updateReport() {
        val action = ReportDetailFragmentDirections.goToReportUpdateAction(args.currentReport)
        findNavController().navigate(action)
    }

    private fun rotateImage(bitmap: Bitmap) {

        val exif = ExifInterface(args.currentReport.photo_path)
        val orientation: Int =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        Log.i("orientation", orientation.toString())

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                matrix.setRotate(90F)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                matrix.setRotate(180F)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.setRotate(270F)
            }
        }

        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        _binding!!.captureImageView.setImageBitmap(rotatedBitmap)

    }

}