package com.example.demo.repository

import androidx.lifecycle.LiveData
import com.example.demo.dao.ReportDAO
import com.example.demo.model.Report

class ReportsRepository(private val reportDao: ReportDAO) {
    val allReports: LiveData<List<Report>> = reportDao.getReports()
    fun insertReport(report: Report) {
        reportDao.insertReport(report)
    }

    fun updateReport(report: Report) {
        reportDao.updateReport(report)
    }
}