package ua.lumos.mobile.network

import org.junit.Assert.assertEquals
import org.junit.Test

class RegionDtoMapperTest {
    @Test
    fun relativeImageUrlIsResolvedAgainstSecureApiHost() {
        assertEquals(
            "https://lumos.foo/api/region-images/lviv.svg",
            normalizeImageUrl("/api/region-images/lviv.svg")
        )
    }

    @Test
    fun insecureImageUrlIsUpgradedToHttps() {
        assertEquals(
            "https://lumos.foo/api/region-images/if.svg",
            normalizeImageUrl("http://lumos.foo/api/region-images/if.svg")
        )
    }

    @Test
    fun unsupportedOrBrokenImageUrlBecomesEmpty() {
        assertEquals("", normalizeImageUrl("ftp://lumos.foo/image.svg"))
        assertEquals("", normalizeImageUrl("http:///broken.svg"))
        assertEquals("", normalizeImageUrl(""))
    }
}
