package ua.lumos.mobile

import ua.lumos.mobile.data.Region

data class FavoriteRegionsUiState(
    val favoriteIds: Set<String> = emptySet(),
    val regions: List<Region> = emptyList(),
    val isLoading: Boolean = true,
    val error: RegionLoadError? = null
) {
    val favoriteRegions: List<Region>
        get() = regions.filter { region -> region.id in favoriteIds }
}
