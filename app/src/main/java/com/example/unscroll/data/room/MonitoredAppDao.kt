
package com.example.unscroll.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitoredAppDao {

    @Query("SELECT * FROM monitored_apps ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<MonitoredAppEntity>>

    @Query("SELECT * FROM monitored_apps WHERE packageName = :packageName")
    suspend fun getByPackage(packageName: String): MonitoredAppEntity?

    @Insert
    suspend fun save(app: MonitoredAppEntity)

    @Query("DELETE FROM monitored_apps WHERE packageName = :packageName")
    suspend fun delete(packageName: String)
}
