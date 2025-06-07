package com.example.gestioneventos

import com.example.gestioneventos.domain.model.Event
import junit.framework.TestCase.assertEquals
import org.junit.Test

class EventOptimizerTest {
    @Test
    fun `getOptimizedEventsDP returns max priority set`() {


        val eventos = listOf(
            Event("1", "A", 1000, 3000, "Conference", 1),
            Event("2", "B", 2000, 4000, "Meeting", 2),
            Event("3", "C", 3000, 5000, "Conference", 0),
            Event("4", "D", 5000, 6000, "Workshop", 2),
            Event("5", "E", 4000, 7000, "Social", 1)
        )

        val optimized = getOptimizedEventsDP(eventos)


        assertEquals(2, optimized.size)
        assertEquals("B", optimized[0].title)
        assertEquals("D", optimized[1].title)
    }
}


fun getOptimizedEventsDP(events: List<Event>): List<Event> {
    val sortedEvents = events.sortedBy { it.endTime }
    val n = sortedEvents.size
    val p = IntArray(n) { -1 }

    for (i in 1 until events.size) {
        for (j in i - 1 downTo 0) {
            if (sortedEvents[j].endTime <= sortedEvents[i].startTime) {
                p[i] = j
                break
            }
        }
    }

    val dp = IntArray(n)
    dp[0] = sortedEvents[0].priority

    for (i in 1 until events.size) {
        val incl = sortedEvents[i].priority + if (p[i] != -1) dp[p[i]] else 0
        val excl = dp[i - 1]
        dp[i] = maxOf(incl, excl)
    }

    fun build(i: Int): List<Event> {
        if (i < 0) return emptyList()
        val include = sortedEvents[i].priority + if (p[i] != -1) dp[p[i]] else 0
        val exclude = if (i > 0) dp[i - 1] else 0
        return if (include > exclude) {
            build(p[i]) + listOf(sortedEvents[i])
        } else build(i - 1)
    }

    return build(events.size - 1)
}

