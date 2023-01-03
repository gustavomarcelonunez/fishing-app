package com.example.demo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.demo.R
import com.example.demo.adapter.ReportListAdapter
import com.example.demo.viewModel.ReportViewModel
import com.example.demo.model.Report

import com.example.demo.databinding.FragmentMainBinding

class MainFragment : Fragment(), ReportListAdapter.OnReportClickListener {
    private val reportViewModel: ReportViewModel by navGraphViewModels(R.id.app_navigation)
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        _binding!!.loginButton.setOnClickListener{ loginApp() }
        _binding!!.registerTextView.setOnClickListener{ registerApp() }

        val reportAdapter = ReportListAdapter(this)
        reportViewModel.allReports
            .observe(
                viewLifecycleOwner,
                Observer { reports ->
                    reports?.let { reportAdapter.setReports(it) }
                }
            )
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginApp() {
        findNavController().navigate(R.id.loginAction)
    }
    private fun registerApp() {
        findNavController().navigate(R.id.registerAction)
    }
    override fun onItemClick(report: Report) {

    }

}