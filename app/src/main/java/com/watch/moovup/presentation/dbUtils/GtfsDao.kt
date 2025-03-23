package com.watch.moovup.presentation.dbUtils

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.watch.moovup.presentation.model.dbModels.Agency
import com.watch.moovup.presentation.model.dbModels.Route
import com.watch.moovup.presentation.model.dbModels.Stop
import com.watch.moovup.presentation.model.dbModels.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface GtfsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStops(stops: List<Stop>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<Route>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgencies(agencies: List<Agency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<Trip>)

    @Query("SELECT * FROM routes WHERE agencyId = :agencyId")
    suspend fun getRoutesByAgencyId(agencyId: Int): List<Route>

    @Query("SELECT * FROM agencies WHERE agencyId = :agencyId")
    suspend fun getAgencyById(agencyId: Int): Agency?

    @Query("SELECT * FROM trips WHERE routeId = :routeId")
    suspend fun getTripsByRouteId(routeId: Int): List<Trip>

    @Query("SELECT * FROM agencies")
    fun getAllAgenciesFlow(): Flow<List<Agency>>

    @Query("SELECT * FROM stops")
    suspend fun getAllStops(): List<Stop>

    @Query("SELECT * FROM stops WHERE stopCode = :stopCode")
    suspend fun getStopByCode(stopCode: String): Stop?

    @Query("SELECT * FROM routes WHERE routeId = :routeId")
    suspend fun findRouteById(routeId: String): Route?

    @Query(
        """
    SELECT * FROM stops
    ORDER BY 
        (stopLat - :userLat) * (stopLat - :userLat) + (stopLon - :userLon) * (stopLon - :userLon)
    LIMIT :limit
    """
    )
    fun getClosestStops(userLat: Double, userLon: Double, limit: Int = 20): List<Stop>
}