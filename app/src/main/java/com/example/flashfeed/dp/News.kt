package com.example.flashfeed.dp

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)