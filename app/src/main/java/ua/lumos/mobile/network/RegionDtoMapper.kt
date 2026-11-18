package ua.lumos.mobile.network

import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability
import java.net.URI

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
        image_url = normalizeImageUrl(imageUrl),
        availability = safeAvailability,
        available = null,
        schedule_days = null
    )
}

internal fun normalizeImageUrl(rawUrl: String?): String {
    val value = rawUrl?.trim().orEmpty()
    if (value.isEmpty()) return ""

    return runCatching {
        val candidate = when {
            value.startsWith("//") -> "https:$value"
            else -> URI(LumosBaseUrl).resolve(value).toString()
        }
        val uri = URI(candidate)
        if (uri.host.isNullOrBlank()) return@runCatching ""
        when (uri.scheme?.lowercase()) {
            "https" -> uri.toASCIIString()
            "http" -> URI(
                "https",
                uri.userInfo,
                uri.host,
                uri.port,
                uri.path,
                uri.query,
                uri.fragment
            ).toASCIIString()
            else -> ""
        }
    }.getOrDefault("")
}
