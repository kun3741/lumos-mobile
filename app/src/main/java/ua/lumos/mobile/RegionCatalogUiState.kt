package ua.lumos.mobile

import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability
import ua.lumos.mobile.data.regions as regionCatalog

enum class RegionFilter(val availability: RegionAvailability?) {
    ALL(null),
    CONNECTED(RegionAvailability.CONNECTED),
    PLANNED(RegionAvailability.PLANNED),
    PAUSED(RegionAvailability.PAUSED)
}

data class RegionCatalogUiState(
    val selectedFilter: RegionFilter = RegionFilter.ALL,
    val regions: List<Region> = regionCatalog
) {
    val visibleRegions: List<Region>
        get() = regions.filter { region ->
            selectedFilter.availability == null || region.availability == selectedFilter.availability
        }
}
