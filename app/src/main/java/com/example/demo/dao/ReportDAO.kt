package com.example.demo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.demo.model.Report


@Dao
interface ReportDAO {
    @Query("SELECT * from report_table ORDER BY id DESC")
    fun getReports(): LiveData<List<Report>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReport(report: Report)

    @Query("DELETE FROM report_table")
    fun deleteAll()

    @Query("SELECT COUNT(id) FROM report_table")
    fun getCount(): Int

    @Transaction
    @Update
    fun updateReport(report: Report)
}

