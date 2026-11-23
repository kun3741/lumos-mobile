package ua.lumos.mobile

import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability

enum class RegionFilter(val availability: RegionAvailability?) {
    ALL(null),
    CONNECTED(RegionAvailability.CONNECTED),
    PLANNED(RegionAvailability.PLANNED),
    PAUSED(RegionAvailability.PAUSED)
}

enum class RegionLoadError {
    NO_NETWORK,
    SERVER,
    INVALID_RESPONSE
}

data class RegionCatalogUiState(
    val selectedFilter: RegionFilter = RegionFilter.ALL,
    val regions: List<Region> = emptyList(),
    val searchQuery: String = "",
    val appliedSearchQuery: String = "",
    val sortAlphabetically: Boolean = false,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: RegionLoadError? = null
) {
    val isSearchActive: Boolean
        get() = searchQuery.isNotBlank()

    val hasEmptySearchResult: Boolean
        get() = appliedSearchQuery.isNotBlank() && visibleRegions.isEmpty()

    val visibleRegions: List<Region>
        get() {
            val normalizedQuery = appliedSearchQuery.trim()
            val filteredRegions = if (normalizedQuery.isNotEmpty()) {
                regions.filter { region ->
                    region.name.contains(normalizedQuery, ignoreCase = true) ||
                        region.id.contains(normalizedQuery, ignoreCase = true)
                }
            } else {
                regions.filter { region ->
                    selectedFilter.availability == null ||
                        region.availability == selectedFilter.availability
                }
            }
            return if (sortAlphabetically) {
                filteredRegions.sortedBy(Region::name)
            } else {
                filteredRegions
            }
        }
}
