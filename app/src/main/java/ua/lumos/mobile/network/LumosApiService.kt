package ua.lumos.mobile.network

import retrofit2.http.GET

interface LumosApiService {
    @GET("api/regions")
    suspend fun getRegions(): RegionCatalogResponseDto
}
