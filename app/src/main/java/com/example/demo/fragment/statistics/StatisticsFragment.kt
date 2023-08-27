package com.example.demo.fragment.statistics

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.demo.R
import com.example.demo.databinding.FragmentStatisticsBinding
import com.example.demo.model.Report
import com.example.demo.viewModel.ReportViewModel
import java.util.*

class StatisticsFragment : Fragment() {

    private val reportViewModel: ReportViewModel by navGraphViewModels(R.id.app_navigation)
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    @RequiresApi(Build.VERSION_CODES.N)
    private val sdf = SimpleDateFormat("d/M/yyyy")
    private val delim = "/"

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

        var currentMonthReports = 0
        var lastSixMonthsReports = 0

        val species = resources.getStringArray(R.array.species)
        var theSpeciesMap = createMutableMapOf(species)

        if (!reportList.isEmpty()) {
            _binding!!.startActivityTextView.text = getFirstDayOfReportList(reportList)
            for (report in reportList) {
                val splitedDate = report.date.split(delim)
                if (splitedDate[1] == currentMonth.toString() && splitedDate[2] == currentYear.toString()) {
                    currentMonthReports += 1
                }

                val actualReportDate: Date = sdf.parse(report.date)
                lastSixMonthsReports += checkForSixMonthsReport(actualReportDate)
                theSpeciesMap[report.specie] = (theSpeciesMap[report.specie] ?: 0) + 1
            }

            _binding!!.currentMonthTextView.text = currentMonthReports.toString()
            _binding!!.sixMonthsTextView.text = lastSixMonthsReports.toString()

            //Extract function from here
            val delimEqual = "="
            var maxBy = theSpeciesMap.maxBy { it.value }
            var splitedMaxBy = maxBy.toString().split(delimEqual)

            _binding!!.firstPlaceTextView.text = splitedMaxBy[0] + " (" + splitedMaxBy[1] + ")"

            theSpeciesMap.remove(splitedMaxBy[0])
            maxBy = theSpeciesMap.maxBy { it.value }

            splitedMaxBy = maxBy.toString().split(delimEqual)
            if(splitedMaxBy[1] == "0") {
                _binding!!.secondPlaceTextView.text = "-"
            } else {
                _binding!!.secondPlaceTextView.text = splitedMaxBy[0] + " (" + splitedMaxBy[1] + ")"
            }
            theSpeciesMap.remove(splitedMaxBy[0])
            maxBy = theSpeciesMap.maxBy { it.value }

            splitedMaxBy = maxBy.toString().split(delimEqual)
            if(splitedMaxBy[1] == "0") {
                _binding!!.thirdPlaceTextView.text = "-"
            } else {
                _binding!!.thirdPlaceTextView.text = splitedMaxBy[0] + " (" + splitedMaxBy[1] + ")"
            }
            //To here

            _binding!!.totalReportsTextView.text = reportList.size.toString()
            _binding!!.lastCaptureTextView.text = reportList.first().specie
        } else {
            _binding!!.startActivityTextView.text = "Aún no existen registros"
            _binding!!.currentMonthTextView.text = "0"
            _binding!!.sixMonthsTextView.text = "0"
            _binding!!.totalReportsTextView.text = "0"
            _binding!!.firstPlaceTextView.text = "-"
            _binding!!.secondPlaceTextView.text = "-"
            _binding!!.thirdPlaceTextView.text = "-"
            _binding!!.lastCaptureTextView.text = "-"
        }
    }

    private fun checkForSixMonthsReport(date: Date): Int {
        var count = 0

        if(currentMonth <= 6) {
            val lastYear = currentYear - 1
            val sixMonthsAgo = 12 - (6 - currentMonth)
            val auxDate: Date = sdf.parse("1/$sixMonthsAgo/$lastYear")

            val cmpDate = date.compareTo(auxDate)
            when {
                cmpDate >= 0 -> {
                    count += 1
                }
            }
        }
        else if (currentMonth > 6) {
            val sixMonthsAgo = currentMonth - 6
            val auxDate: Date = sdf.parse("1/$sixMonthsAgo/$currentYear")

            val cmpDate = date.compareTo(auxDate)
            when {
                cmpDate >= 0 -> {
                    count += 1
                }
            }
        }
        return count
    }

    private fun createMutableMapOf(species: Array<String>): MutableMap<String, Int> {
        var aMutableMapOfSpecies = mutableMapOf<String, Int>()
        for (specie in species) {
            aMutableMapOfSpecies[specie] = 0
        }
        return aMutableMapOfSpecies
    }

    private fun getFirstDayOfReportList(reports: List<Report>): CharSequence {
        if (reports.isEmpty()) {
            return "No hay reportes cargados"
        }
        return reports.last().date
    }

    private fun goToMultipleMaps() {
        findNavController().navigate(R.id.goToMultipleMapsFragmentFromStatisticsAction)
    }
    private fun goBack() {
        findNavController().popBackStack()
    }

}