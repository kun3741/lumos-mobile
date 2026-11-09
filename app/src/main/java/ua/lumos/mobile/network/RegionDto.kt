package ua.lumos.mobile.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionCatalogResponseDto(
    val regions: List<RegionDto> = emptyList()
)

@Serializable
data class RegionDto(
    val id: String? = null,
    val name: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val availability: String? = null
)
