package com.seoultech.mobileprogramming.high_five.DTO

import android.media.AudioTimestamp
import android.net.Uri

data class Post(
    val imageDownloadUri: String,
    val contents: String,
    val friendUserId: String,
    val timestamp: Long,
    val location: String,
)
