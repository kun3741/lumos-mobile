package ua.lumos.mobile.network

import retrofit2.http.GET
import retrofit2.http.Query

interface LumosApiService {
    @GET("api/regions")
    suspend fun getRegions(): RegionCatalogResponseDto

    @GET("api/schedule")
    suspend fun getSchedule(
        @Query("region") region: String,
        @Query("queue") queue: String
    ): ScheduleResponseDto
}
