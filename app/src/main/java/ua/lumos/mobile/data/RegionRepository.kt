package ua.lumos.mobile.data

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import ua.lumos.mobile.network.LumosApiClient
import ua.lumos.mobile.network.LumosApiService
import ua.lumos.mobile.network.toDomainOrNull

class RegionRepository(
    private val api: LumosApiService = LumosApiClient.service
) {
    private val scheduleRequestLimiter = Semaphore(MaxConcurrentScheduleRequests)

    suspend fun getRegions(): List<Region> {
        val regions = api.getRegions()
            .regions
            .mapNotNull { regionDto -> regionDto.toDomainOrNull() }

        check(regions.map(Region::id).distinct().size == regions.size) {
            "Region ids returned by the API must be unique"
        }

        return coroutineScope {
            regions.map { region ->
                async {
                    scheduleRequestLimiter.withPermit {
                        try {
                            val schedule = api.getSchedule(
                                region = region.id,
                                queue = CatalogPreviewQueue
                            )
                            region.copy(
                                available = schedule.available,
                                schedule_days = schedule.days.size
                            )
                        } catch (exception: CancellationException) {
                            throw exception
                        } catch (_: Exception) {
                            region
                        }
                    }
                }
            }.awaitAll()
        }
    }
}

private const val CatalogPreviewQueue = "1.1"
private const val MaxConcurrentScheduleRequests = 3
