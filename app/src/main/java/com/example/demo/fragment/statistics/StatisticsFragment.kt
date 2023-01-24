package com.example.demo.fragment.statistics

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.demo.R
import com.example.demo.databinding.FragmentStatisticsBinding
import com.example.demo.model.Report
import com.example.demo.viewModel.ReportViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class StatisticsFragment : Fragment() {

    private val reportViewModel: ReportViewModel by navGraphViewModels(R.id.app_navigation)
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        val reportList = reportViewModel.allReports.value!!
        fillLabels(reportList)

        _binding!!.goBackButton.setOnClickListener { goBack() }
        _binding!!.goToMultipleMapsButton.setOnClickListener { goToMultipleMaps() }
        val view = binding.root
        return view
    }

    private fun fillLabels(reportList: List<Report>) {
        _binding!!.startActivityTextView.text = reportList.last().date

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        var currentMonthReports = 0

        var lastSixMonthsReports = 0
        val sdf = SimpleDateFormat("d/M/yyyy")

        val delim = "/"

        // TODO
        // Calcular 1, 2 y 3 puesto de capturas

        _binding!!.firstPlaceTextView.text = "pejerrey (10)"
        _binding!!.secondPlaceTextView.text = "surubi (8)"
        _binding!!.thirdPlaceTextView.text = "bagre (3)"


        for (report in reportList) {
            val splitedDate = report.date.split(delim)

            if(splitedDate[1] == currentMonth.toString()) {
                currentMonthReports += 1
            }

            val actualReportDate: Date = sdf.parse(report.date)

            if(currentMonth <= 6) {
                val lastYear = currentYear - 1
                val sixMonthsAgo = 12 - (6 - currentMonth)
                val auxDate: Date = sdf.parse("1/$sixMonthsAgo/$lastYear")

                val cmpDate = actualReportDate.compareTo(auxDate)
                when {
                    cmpDate >= 0 -> {
                        lastSixMonthsReports += 1
                    }
                }
            }
            else if (currentMonth > 6) {
                val sixMonthsAgo = currentMonth - 6
                val auxDate: Date = sdf.parse("1/$sixMonthsAgo/$currentYear")

                val cmpDate = actualReportDate.compareTo(auxDate)
                when {
                    cmpDate >= 0 -> {
                        lastSixMonthsReports += 1
                    }
                }
            }


        }

        _binding!!.lastMonthTextView.text = currentMonthReports.toString()
        _binding!!.sixMonthsTextView.text = lastSixMonthsReports.toString()
        _binding!!.totalReportsTextView.text = reportList.size.toString()
        _binding!!.lastCaptureTextView.text = reportList.first().specie
    }


    private fun goToMultipleMaps() {
        findNavController().navigate(R.id.goToMultipleMapsFragmentFromStatisticsAction)
    }
    private fun goBack() {
        findNavController().popBackStack()
    }

}