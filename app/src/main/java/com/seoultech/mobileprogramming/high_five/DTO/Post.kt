package com.seoultech.mobileprogramming.high_five.DTO

import android.media.AudioTimestamp
import android.net.Uri

data class Post(val photoUri: String, val contents: String, val timestamp: Long)
data class PostWithoutImage(val contents:String)