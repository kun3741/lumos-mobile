package ua.lumos.mobile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability

class RegionCatalogUiStateTest {
    private val regions = listOf(
        region("if", "Івано-Франківщина", RegionAvailability.CONNECTED),
        region("lviv", "Львівщина", RegionAvailability.CONNECTED),
        region("planned", "Тестовий регіон", RegionAvailability.PLANNED)
    )

    @Test
    fun searchMatchesNameIgnoringCaseAndIgnoresCategoryFilter() {
        val state = RegionCatalogUiState(
            selectedFilter = RegionFilter.PLANNED,
            regions = regions,
            searchQuery = "ЛЬВ",
            appliedSearchQuery = "ЛЬВ",
            isLoading = false
        )

        assertEquals(listOf("lviv"), state.visibleRegions.map(Region::id))
    }

    @Test
    fun searchAlsoMatchesRegionCode() {
        val state = RegionCatalogUiState(
            regions = regions,
            searchQuery = "IF",
            appliedSearchQuery = "IF",
            isLoading = false
        )

        assertEquals(listOf("if"), state.visibleRegions.map(Region::id))
    }

    @Test
    fun blankSearchReturnsToSelectedCategory() {
        val state = RegionCatalogUiState(
            selectedFilter = RegionFilter.PLANNED,
            regions = regions,
            searchQuery = "   ",
            appliedSearchQuery = "",
            isLoading = false
        )

        assertEquals(listOf("planned"), state.visibleRegions.map(Region::id))
    }

    @Test
    fun missingSearchResultHasDedicatedState() {
        val state = RegionCatalogUiState(
            regions = regions,
            searchQuery = "Одеса",
            appliedSearchQuery = "Одеса",
            isLoading = false
        )

        assertTrue(state.visibleRegions.isEmpty())
        assertTrue(state.hasEmptySearchResult)
    }

    private fun region(
        id: String,
        name: String,
        availability: RegionAvailability
    ) = Region(
        id = id,
        name = name,
        image_url = "",
        availability = availability,
        available = null,
        schedule_days = null
    )
}
