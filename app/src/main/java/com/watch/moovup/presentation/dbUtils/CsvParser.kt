package com.watch.moovup.presentation.dbUtils

import android.content.Context
import com.watch.moovup.presentation.MainActivity
import com.watch.moovup.presentation.model.dbModels.Agency
import com.watch.moovup.presentation.model.dbModels.Route
import com.watch.moovup.presentation.model.dbModels.Stop
import com.watch.moovup.presentation.model.dbModels.Trip
import java.io.BufferedReader
import java.io.InputStreamReader

object CsvParser {
    fun readStopsCsvFile(context: Context, fileName: String = "stops.txt"): List<Stop> {
        val stops = mutableListOf<Stop>()
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine() // skip header

            line = reader.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.size >= 9) {
                    val stop = Stop(
                        stopId = tokens[0].toInt(),
                        stopCode = tokens[1].toInt(),
                        stopName = tokens[2],
                        stopDesc = tokens[3],
                        stopLat = tokens[4].toDouble(),
                        stopLon = tokens[5].toDouble(),
                        locationType = tokens[6].toInt(),
                        parentStation = tokens[7].takeIf { it.isNotEmpty() },
                        zoneId = tokens[8].toInt()
                    )
                    stops.add(stop)
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stops
    }

    fun readRoutesCsvFile(context: Context, fileName: String = "routes.txt"): List<Route> {
        val routes = mutableListOf<Route>()
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine() // skip header
            line = reader.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.size >= 7) {
                    val route = Route(
                        routeId = tokens[0].toInt(),
                        agencyId = tokens[1].toInt(),
                        routeShortName = tokens[2],
                        routeLongName = tokens[3],
                        routeDesc = tokens[4].takeIf { it.isNotEmpty() },
                        routeType = tokens[5].toInt(),
                        routeColor = tokens[6].takeIf { it.isNotEmpty() }
                    )
                    routes.add(route)
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return routes
    }

    fun readAgenciesCsvFile(context: Context, fileName: String = "agency.txt"): List<Agency> {
        val agencies = mutableListOf<Agency>()
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine() // skip header
            line = reader.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.size >= 7) {
                    val agency = Agency(
                        agencyId = tokens[0].toInt(),
                        agencyName = tokens[1],
                        agencyUrl = tokens[2],
                        agencyTimezone = tokens[3],
                        agencyLang = tokens[4],
                        agencyPhone = tokens[5].takeIf { it.isNotEmpty() },
                        agencyFareUrl = tokens[6].takeIf { it.isNotEmpty() }
                    )
                    agencies.add(agency)
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return agencies
    }

    fun readTripsCsvFile(context: Context, fileName: String = "trips.txt"): List<Trip> {
        val trips = mutableListOf<Trip>()
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine() // skip header
            line = reader.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.size >= 7) {
                    val trip = Trip(
                        routeId = tokens[0].toInt(),
                        serviceId = tokens[1].toInt(),
                        tripId = tokens[2],
                        tripHeadsign = tokens[3],
                        directionId = tokens[4].toInt(),
                        shapeId = tokens[5].toInt(),
                        wheelchairAccessible = tokens[6].toInt()
                    )
                    trips.add(trip)
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return trips
    }
}