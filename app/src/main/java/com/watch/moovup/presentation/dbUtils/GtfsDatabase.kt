package com.watch.moovup.presentation.dbUtils

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.watch.moovup.presentation.model.dbModels.Agency
import com.watch.moovup.presentation.model.dbModels.Route
import com.watch.moovup.presentation.model.dbModels.Stop
import com.watch.moovup.presentation.model.dbModels.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt

@Database(entities = [Stop::class, Route::class, Agency::class, Trip::class], version = 1)
abstract class GtfsDatabase : RoomDatabase() {
    abstract fun gtfsDao(): GtfsDao

    companion object {
        private var instance: GtfsDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): GtfsDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, GtfsDatabase::class.java,
                    "gtfs-database"
                )
                    .fallbackToDestructiveMigration()
                    .createFromAsset("gtfs-database.db")
                    .build()

            return instance!!
        }
    }

    suspend fun populateDatabase(ctx: Context) {
        val stops = CsvParser.readStopsCsvFile(ctx)
        val routes = CsvParser.readRoutesCsvFile(ctx)
        val agencies = CsvParser.readAgenciesCsvFile(ctx)
        val trips = CsvParser.readTripsCsvFile(ctx)

        gtfsDao().insertStops(stops)
        gtfsDao().insertAgencies(agencies)
        gtfsDao().insertRoutes(routes)
        gtfsDao().insertTrips(trips)
    }

}