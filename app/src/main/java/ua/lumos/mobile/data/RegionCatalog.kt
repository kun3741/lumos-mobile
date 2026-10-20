package ua.lumos.mobile.data

val regions = listOf(
    Region(
        id = "if",
        name = "Івано-Франківщина",
        image_url = "https://lumos.foo/api/region-images/if.svg",
        availability = RegionAvailability.CONNECTED
    ),
    Region(
        id = "lviv",
        name = "Львівщина",
        image_url = "https://lumos.foo/api/region-images/lviv.svg",
        availability = RegionAvailability.CONNECTED
    ),
    Region(
        id = "volyn",
        name = "Волинь",
        image_url = "https://lumos.foo/api/region-images/volyn.svg",
        availability = RegionAvailability.CONNECTED
    ),
    Region(
        id = "ternopil",
        name = "Тернопільщина",
        image_url = "https://lumos.foo/api/region-images/ternopil.svg",
        availability = RegionAvailability.PLANNED
    ),
    Region(
        id = "rivne",
        name = "Рівненщина",
        image_url = "https://lumos.foo/api/region-images/rivne.svg",
        availability = RegionAvailability.PLANNED
    ),
    Region(
        id = "kyiv",
        name = "Київщина",
        image_url = "https://lumos.foo/api/region-images/kyiv.svg",
        availability = RegionAvailability.PLANNED
    )
)
