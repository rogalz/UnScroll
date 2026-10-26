package com.example.unscroll.data.model

data class UsageStatItem(
    val packageName: String,
    val appName: String,
    val totalTimeInForeground: Long,
    val lastTimeUsed: Long,
    val firstTimeStamp: Long,
    val lastTimeStamp: Long,
)