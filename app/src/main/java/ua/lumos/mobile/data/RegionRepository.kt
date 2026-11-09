package ua.lumos.mobile.data

import ua.lumos.mobile.network.LumosApiClient
import ua.lumos.mobile.network.LumosApiService
import ua.lumos.mobile.network.toDomainOrNull

class RegionRepository(
    private val api: LumosApiService = LumosApiClient.service
) {
    suspend fun getRegions(): List<Region> = api.getRegions()
        .regions
        .mapNotNull { regionDto -> regionDto.toDomainOrNull() }
}
