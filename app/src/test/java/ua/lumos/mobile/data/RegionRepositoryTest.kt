package ua.lumos.mobile.data

import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ua.lumos.mobile.network.LumosApiService
import ua.lumos.mobile.network.RegionCatalogResponseDto
import ua.lumos.mobile.network.RegionDto
import ua.lumos.mobile.network.ScheduleDayDto
import ua.lumos.mobile.network.ScheduleResponseDto

class RegionRepositoryTest {
    @Test
    fun schedulePreviewsLoadInParallelWithThreeRequestLimit() = runBlocking {
        val activeRequests = AtomicInteger(0)
        val peakRequests = AtomicInteger(0)
        val api = object : LumosApiService {
            override suspend fun getRegions() = RegionCatalogResponseDto(
                regions = (1..7).map { index ->
                    RegionDto(
                        id = "region-$index",
                        name = "Region $index",
                        imageUrl = "/images/$index.svg"
                    )
                }
            )

            override suspend fun getSchedule(
                region: String,
                queue: String
            ): ScheduleResponseDto {
                val active = activeRequests.incrementAndGet()
                peakRequests.updateAndGet { current -> maxOf(current, active) }
                return try {
                    delay(40)
                    ScheduleResponseDto(
                        available = true,
                        days = listOf(ScheduleDayDto("2026-11-18"))
                    )
                } finally {
                    activeRequests.decrementAndGet()
                }
            }
        }

        val regions = RegionRepository(api).getRegions()

        assertEquals(7, regions.size)
        assertTrue(peakRequests.get() > 1)
        assertTrue(peakRequests.get() <= 3)
        assertTrue(regions.all { it.available == true && it.schedule_days == 1 })
    }
}
