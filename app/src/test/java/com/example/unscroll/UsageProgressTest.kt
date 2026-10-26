package com.example.unscroll

import com.example.unscroll.domain.monitoring.UsageProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UsageProgressTest {
    @Test
    fun exactLimitIsNotExceeded() {
        val progress = UsageProgress.from(
            usedMillis = 30 * 60_000L,
            limitMinutes = 30,
        )

        assertEquals(0, progress.remainingMinutes)
        assertEquals(1f, progress.fraction, 0f)
        assertEquals(100, progress.percentage)
        assertFalse(progress.exceeded)
    }

    @Test
    fun usageOverLimitIsReportedAndProgressIsCapped() {
        val progress = UsageProgress.from(
            usedMillis = 35 * 60_000L,
            limitMinutes = 30,
        )

        assertEquals(0, progress.remainingMinutes)
        assertEquals(1f, progress.fraction, 0f)
        assertEquals(116, progress.percentage)
        assertTrue(progress.exceeded)
    }
}
