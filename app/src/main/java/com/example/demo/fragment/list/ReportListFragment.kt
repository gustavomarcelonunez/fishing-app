package com.example.demo.fragment.list

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.adapter.ReportListAdapter
import com.example.demo.databinding.FragmentReportListBinding
import com.example.demo.model.Report
import com.example.demo.viewModel.ReportViewModel

class ReportListFragment : Fragment(), ReportListAdapter.OnReportClickListener {

    private val reportViewModel: ReportViewModel by navGraphViewModels(R.id.app_navigation)
    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentReportListBinding.inflate(inflater, container, false)
        _binding!!.homeActionButton.setOnClickListener { goHome() }

        loadFullList()

        val view = binding.root
        return view
    }

    override fun onItemClick(report: Report) {
        val action = ReportListFragmentDirections.goToReportDetailFromMyReportsAction(report)
        findNavController().navigate(action)
    }

    private fun goHome() {
        findNavController().navigate(R.id.home_fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.report_filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return if (id == R.id.report_action_filter) {

            val dpd = DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val dateSelected = "" + dayOfMonth + "/" + (monthOfYear + 1)  + "/" + year
                    loadListWithDate(dateSelected)
                }, year, month, day
            )
            dpd.show()
            true
        } else return if (id == R.id.report_action_clear_filter) {
            loadFullList()
            true
        } else super.onOptionsItemSelected(item)

    }

    private fun loadFullList() {
        val reportList: RecyclerView = binding.list
        val reportAdapter = ReportListAdapter(this)
        reportList.adapter = reportAdapter

        reportViewModel.allReports
            .observe(
                viewLifecycleOwner,
                Observer { reports ->
                    reports?.let { reportAdapter.setReports(it) }
                }
            )

    }

    private fun loadListWithDate(date: String) {
        val reportList: RecyclerView = binding.list
        val reportAdapter = ReportListAdapter(this)
        reportList.adapter = reportAdapter

        reportViewModel.allReports
            .observe(
                viewLifecycleOwner,
                Observer { reports ->
                    val filteredList = remove(reports, date)
                    reports?.let { reportAdapter.setReports(filteredList) }
                }
            )
    }

    private fun remove(arr: List<Report>, target: String): List<Report> {
        val result: MutableList<Report> = ArrayList()

        for (report in arr) {
            if (report.date == target) {
                result.add(report)
            }
        }
        return result
    }
}