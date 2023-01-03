package com.example.demo.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.demo.dao.ReportDAO
import com.example.demo.model.Report
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Report::class), version = 1, exportSchema = false)
@TypeConverters(Converter::class)
public abstract class ReportRoomDatabase : RoomDatabase() {

    abstract fun reportDao(): ReportDAO

    companion object {


        @Volatile
        private var INSTANCIA: ReportRoomDatabase? = null

        fun getDatabase(
            context: Context,
            viewModelScope: CoroutineScope
        ): ReportRoomDatabase {
            val instanciaTemporal = INSTANCIA
            if (instanciaTemporal != null) {
                return instanciaTemporal
            }
            synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    ReportRoomDatabase::class.java,
                    "report_database"
                )
                    .addCallback(ReportDatabaseCallback(viewModelScope))
                    .build()
                INSTANCIA = instancia
                return instancia
            }
        }

        private class ReportDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCIA?.let { database ->
                    scope.launch {
//                        populateDatabase(database.reportDao())
                    }
                }
            }
            suspend fun populateDatabase(reportDAO: ReportDAO) {
                if (reportDAO.getCount() == 0) {
                    reportDAO.deleteAll()

                }
            }
        }
    }
}