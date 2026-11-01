package ua.lumos.mobile.data

data class Region(
    val id: String,
    val name: String,
    val image_url: String,
    val availability: RegionAvailability,
    val provider: String,
    val supported_queues: String,
    val update_frequency: String,
    val description: String,
    val notes: String
)

enum class RegionAvailability {
    CONNECTED,
    PLANNED,
    PAUSED
}
