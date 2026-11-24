package ua.lumos.mobile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability

class FavoriteRegionsUiStateTest {
    private val regions = listOf(
        region("if", "Івано-Франківщина"),
        region("lviv", "Львівщина"),
        region("volyn", "Волинь")
    )

    @Test
    fun collectionResolvesObjectsOnlyBySavedIds() {
        val state = FavoriteRegionsUiState(
            favoriteIds = setOf("lviv", "volyn"),
            regions = regions,
            isLoading = false
        )

        assertEquals(listOf("lviv", "volyn"), state.favoriteRegions.map(Region::id))
    }

    @Test
    fun staleOrUnknownIdsDoNotCreateStoredObjectCopies() {
        val state = FavoriteRegionsUiState(
            favoriteIds = setOf("missing"),
            regions = regions,
            isLoading = false
        )

        assertTrue(state.favoriteRegions.isEmpty())
    }

    private fun region(id: String, name: String) = Region(
        id = id,
        name = name,
        image_url = "",
        availability = RegionAvailability.CONNECTED,
        available = null,
        schedule_days = null
    )
}
