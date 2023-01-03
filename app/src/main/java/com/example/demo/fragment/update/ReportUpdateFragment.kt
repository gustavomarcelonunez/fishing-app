package com.example.demo.fragment.update

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.demo.R
import com.example.demo.databinding.FragmentReportUpdateBinding
import com.example.demo.fragment.add.REQUEST_TAKE_PHOTO
import com.example.demo.model.Report
import com.example.demo.viewModel.ReportViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.min

class ReportUpdateFragment : Fragment() {

    private var _binding: FragmentReportUpdateBinding? = null
    private val binding get() = _binding!!
    private val args: ReportUpdateFragmentArgs by navArgs()
    lateinit var currentPhotoPath: String

    private lateinit var model: ReportViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportUpdateBinding.inflate(inflater, container, false)
        val view = binding.root
        model = ViewModelProvider(this)[ReportViewModel::class.java]

        val fishType = resources.getStringArray(R.array.fishType)
        val species = resources.getStringArray(R.array.species)
        val fishTypeArrayAdapter = ArrayAdapter(view.context, R.layout.dropdown_item, fishType)
        val speciesArrayAdapter = ArrayAdapter(view.context, R.layout.dropdown_item, species)

        currentPhotoPath = args.currentReport.photo_path
        _binding!!.spinner.adapter = fishTypeArrayAdapter
        _binding!!.spinnerFishSpecies.adapter = speciesArrayAdapter
        _binding!!.helpButton.setOnClickListener { fishingInfo() }
        _binding!!.photoButton.setOnClickListener { takePhoto() }
        _binding!!.continueButton.setOnClickListener { continueToMap() }
        val fishTypeArrayPosition = fishTypeArrayAdapter.getPosition(args.currentReport.fishing_type)
        _binding!!.spinner.setSelection(fishTypeArrayPosition)

        val fishSpecieArrayPosition = speciesArrayAdapter.getPosition(args.currentReport.specie)
        _binding!!.spinnerFishSpecies.setSelection(fishSpecieArrayPosition)

        _binding!!.updateTitleTextInput.setText(args.currentReport.title)

        val file = File(args.currentReport.photo_path)
        if (file.exists()) {
            val imageBitmap: Bitmap? = BitmapFactory.decodeFile(args.currentReport.photo_path)
            rotateImage(imageBitmap!!)
        }
        return view
    }

    private fun fishingInfo() {
        findNavController().navigate(R.id.fishing_info_activity)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(activity, ": error", Toast.LENGTH_LONG).show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        "com.example.demo.fileprovider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"

        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        currentPhotoPath = image.absolutePath

        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == AppCompatActivity.RESULT_OK) {
            rotateImage(reduceBitmap())

        }
    }

    private fun reduceBitmap(): Bitmap {
        val targetImageViewWidth = _binding!!.updateCaptureImageView.width
        val targetImageViewHeight = _binding!!.updateCaptureImageView.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)

        val cameraWidth = bmOptions.outWidth
        val cameraHeight = bmOptions.outHeight
        val scaleFactor =
            min(cameraWidth / targetImageViewWidth, cameraHeight / targetImageViewHeight)
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
    }

    private fun rotateImage(bitmap: Bitmap) {

        val exif = ExifInterface(currentPhotoPath)
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

        val outputStream: FileOutputStream = FileOutputStream(currentPhotoPath)
        rotatedBitmap?.compress(
            Bitmap.CompressFormat.JPEG,
            80,
            outputStream
        )

        _binding!!.updateCaptureImageView.setImageBitmap(rotatedBitmap)

    }

    private fun continueToMap() {
        val title = _binding?.updateTitleTextInput?.text.toString()
        val fishingType = _binding?.spinner?.selectedItem.toString()
        val specie = _binding?.spinnerFishSpecies?.selectedItem.toString()
        val date = args.currentReport.date

        // If empty, photo path did not change
        val photoPath: String = if (currentPhotoPath == "") {
            args.currentReport.photo_path
        } else {
            currentPhotoPath
        }
        val updatedReport = Report(args.currentReport.id, title, fishingType, specie, date, photoPath, args.currentReport.latitude, args.currentReport.longitude)
        val action = ReportUpdateFragmentDirections.goToMapsFragmentFromReportUpdateFragment(updatedReport)
        findNavController().navigate(action)
    }

}