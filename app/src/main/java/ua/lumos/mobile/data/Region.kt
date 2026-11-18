package ua.lumos.mobile.data

data class Region(
    val id: String,
    val name: String,
    val image_url: String,
    val availability: RegionAvailability,
    val available: Boolean?,
    val schedule_days: Int?
)

enum class RegionAvailability {
    CONNECTED,
    PLANNED,
    PAUSED
}
