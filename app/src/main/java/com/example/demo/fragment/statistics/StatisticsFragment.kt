package com.example.demo.fragment.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.demo.R
import com.example.demo.databinding.FragmentStatisticsBinding
import com.example.demo.viewModel.ReportViewModel

class StatisticsFragment : Fragment() {

    private val reportViewModel: ReportViewModel by navGraphViewModels(R.id.app_navigation)
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        _binding!!.goBackButton.setOnClickListener { goBack() }
        _binding!!.goToMultipleMapsButton.setOnClickListener { goToMultipleMaps() }
        val view = binding.root
        return view
    }

    private fun goToMultipleMaps() {
        findNavController().navigate(R.id.goToMultipleMapsFragmentFromStatisticsAction)
    }
    private fun goBack() {
        findNavController().popBackStack()
    }

}