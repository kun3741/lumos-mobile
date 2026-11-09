package ua.lumos.mobile.network

import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability

fun RegionDto.toDomainOrNull(): Region? {
    val safeId = id?.trim().orEmpty()
    val safeName = name?.trim().orEmpty()
    if (safeId.isEmpty() || safeName.isEmpty()) return null

    val safeAvailability = when (availability?.trim()?.uppercase()) {
        "PLANNED" -> RegionAvailability.PLANNED
        "PAUSED" -> RegionAvailability.PAUSED
        else -> RegionAvailability.CONNECTED
    }

    return Region(
        id = safeId,
        name = safeName,
        image_url = imageUrl?.trim().orEmpty(),
        availability = safeAvailability
    )
}
