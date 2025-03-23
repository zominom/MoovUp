package com.watch.moovup.presentation.api

import com.watch.moovup.presentation.model.apiModels.Quote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SiriApiService {
    @GET("json")
    fun getQuote(
        @Query("Key") key: String,
        @Query("MonitoringRef") monitoringRef: String,
        @Query("StopVisitDetailLevel") stopMonitoringDetailLevel: String? = null,
//        @Query("LineRef") lineRef: String? = null,
//        @Query("PreviewInternal") previewInternal: String = "PT30M"
        // @Query("StartTime") startTime: String = "YYYYMMDDTHHmmSSPSS"
        // @Query("MaximumStopVisits") maximumStopVisits: String = "${LIMIT}"
        // And more Look at https://www.gov.il/BlobFolder/generalpage/real_time_information_siri/he/ICD_SM_28_31.pdf for further information.

    ): Call<Quote>
}