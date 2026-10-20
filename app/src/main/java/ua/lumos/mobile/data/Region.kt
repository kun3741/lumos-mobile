package ua.lumos.mobile.data

data class Region(
    val id: String,
    val name: String,
    val image_url: String,
    val availability: RegionAvailability
)

enum class RegionAvailability {
    CONNECTED,
    PLANNED,
    PAUSED
}
