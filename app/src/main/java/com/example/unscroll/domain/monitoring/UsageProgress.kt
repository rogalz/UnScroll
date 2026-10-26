
package com.example.unscroll.domain.monitoring

data class UsageProgress(
    val usedMinutes: Int,
    val limitMinutes: Int,
) {
    val remainingMinutes: Int
        get() = (limitMinutes - usedMinutes).coerceAtLeast(0)

    val fraction: Float
        get() = (usedMinutes.toFloat() / limitMinutes)
            .coerceIn(0f, 1f)

    val exceeded: Boolean
        get() = usedMinutes > limitMinutes

    val percentage: Int
        get() = (usedMinutes * 100f / limitMinutes).toInt()

    companion object {
        fun from(
            usedMillis: Long,
            limitMinutes: Int,
        ): UsageProgress {
            require(limitMinutes > 0)

            return UsageProgress(
                usedMinutes = (usedMillis / 60_000L).toInt(),
                limitMinutes = limitMinutes,
            )
        }
    }
}
