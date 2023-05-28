package com.seoultech.mobileprogramming.high_five.DTO

data class Post(
    val imageDownloadUri: String,
    val contents: String,
    val friendUserId: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)
