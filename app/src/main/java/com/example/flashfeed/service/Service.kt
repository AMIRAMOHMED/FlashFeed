package com.example.flashfeed.service

import com.example.flashfeed.Utils.Companion.API_KEY
import com.example.flashfeed.dp.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {


    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ):Response<News>

    @GET("v2/everything")
    suspend fun getByCategory(
        @Query("q")
        category: String = "",
        @Query("apiKey")
        apiKey : String = API_KEY
    ): Response<News>
}