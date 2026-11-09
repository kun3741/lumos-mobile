package ua.lumos.mobile.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object LumosApiClient {
    private const val BaseUrl = "https://lumos.foo/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val service: LumosApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(LumosApiService::class.java)
    }
}
