
package com.example.unscroll.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monitored_apps")
data class MonitoredAppEntity(
    @PrimaryKey
    val packageName: String,

    val dailyLimitMinutes: Int,

    val createdAt: Long = System.currentTimeMillis(),
)
