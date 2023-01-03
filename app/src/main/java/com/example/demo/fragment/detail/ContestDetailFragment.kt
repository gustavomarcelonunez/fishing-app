package com.example.demo.fragment.detail

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.demo.viewModel.ContestViewModel
import com.example.demo.R
import com.example.demo.databinding.FragmentContestDetailBinding

class ContestDetailFragment : Fragment() {
    private var _binding: FragmentContestDetailBinding? = null
    private val binding get() = _binding!!
    private val model: ContestViewModel by navGraphViewModels(R.id.app_navigation)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContestDetailBinding.inflate(inflater, container, false)
        _binding!!.titleTextView.text = model.title.value
        _binding!!.descriptionTextView.text = model.description.value
        _binding!!.dateTextView.text = model.date.value
        _binding!!.termsAndConditionsTextView.text = model.termsAndConditions.value

        val image = model.image.value
        _binding!!.captureImageView.setImageBitmap(image)

        _binding!!.goBackButton.setOnClickListener { goBack() }

        val view = binding.root
        return view
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

}