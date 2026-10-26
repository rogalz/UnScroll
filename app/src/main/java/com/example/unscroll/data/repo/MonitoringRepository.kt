
package com.example.unscroll.data.repo

import com.example.unscroll.data.room.MonitoredAppDao
import com.example.unscroll.data.room.MonitoredAppEntity
import kotlinx.coroutines.flow.Flow

class MonitoringRepository(
    private val dao: MonitoredAppDao,
) {
    fun observeAll(): Flow<List<MonitoredAppEntity>> =
        dao.observeAll()

    suspend fun getByPackage(
        packageName: String,
    ): MonitoredAppEntity? =
        dao.getByPackage(packageName)

    suspend fun save(
        packageName: String,
        dailyLimitMinutes: Int,
    ) {
        require(dailyLimitMinutes > 0) {
            "Limit musi być większy od zera."
        }
        check(dao.getByPackage(packageName) == null) {
            "Ta aplikacja jest już monitorowana."
        }

        dao.save(
            MonitoredAppEntity(
                packageName = packageName,
                dailyLimitMinutes = dailyLimitMinutes,
            )
        )
    }

    suspend fun remove(packageName: String) {
        dao.delete(packageName)
    }
}
