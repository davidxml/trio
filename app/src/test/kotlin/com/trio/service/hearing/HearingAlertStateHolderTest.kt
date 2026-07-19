package com.trio.service.hearing

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HearingAlertStateHolderTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var holder: HearingAlertStateHolder

    @Before
    fun setup() {
        holder = HearingAlertStateHolder()
    }

    @Test
    fun `pushCaption adds caption to recentCaptions`() = testScope.runTest {
        holder.pushCaption(text = "Hello", source = "System")

        val captions = holder.recentCaptions.value
        assertEquals(1, captions.size)
        assertEquals("Hello", captions[0].text)
        assertEquals("System", captions[0].source)
    }

    @Test
    fun `pushCaption prepends newest caption`() = testScope.runTest {
        holder.pushCaption(text = "First", source = "System")
        holder.pushCaption(text = "Second", source = "Screen")

        val captions = holder.recentCaptions.value
        assertEquals(2, captions.size)
        assertEquals("Second", captions[0].text)
        assertEquals("First", captions[1].text)
    }

    @Test
    fun `pushCaption respects MAX_CAPTIONS limit`() = testScope.runTest {
        repeat(25) { i ->
            holder.pushCaption(text = "Caption $i", source = "System")
        }

        val captions = holder.recentCaptions.value
        assertEquals(20, captions.size)
    }

    @Test
    fun `pushAlert adds alert to pendingAlerts`() = testScope.runTest {
        holder.pushAlert(title = "Title", body = "Body", importance = 1)

        val alerts = holder.pendingAlerts.value
        assertEquals(1, alerts.size)
        assertEquals("Title", alerts[0].title)
        assertEquals("Body", alerts[0].body)
        assertEquals(1, alerts[0].importance)
    }

    @Test
    fun `pushAlert prepends newest alert`() = testScope.runTest {
        holder.pushAlert(title = "First", body = "Body1", importance = 1)
        holder.pushAlert(title = "Second", body = "Body2", importance = 2)

        val alerts = holder.pendingAlerts.value
        assertEquals(2, alerts.size)
        assertEquals("Second", alerts[0].title)
        assertEquals("First", alerts[1].title)
    }

    @Test
    fun `pushAlert respects MAX_ALERTS limit`() = testScope.runTest {
        repeat(15) { i ->
            holder.pushAlert(title = "Alert $i", body = "Body", importance = 1)
        }

        val alerts = holder.pendingAlerts.value
        assertEquals(10, alerts.size)
    }

    @Test
    fun `dismissAlert removes alert at given index`() = testScope.runTest {
        holder.pushAlert(title = "First", body = "Body1", importance = 1)
        holder.pushAlert(title = "Second", body = "Body2", importance = 2)

        holder.dismissAlert(0)

        val alerts = holder.pendingAlerts.value
        assertEquals(1, alerts.size)
        assertEquals("First", alerts[0].title)
    }

    @Test
    fun `dismissAlert does nothing for out-of-bounds index`() = testScope.runTest {
        holder.pushAlert(title = "First", body = "Body", importance = 1)

        holder.dismissAlert(5)

        assertEquals(1, holder.pendingAlerts.value.size)
    }

    @Test
    fun `dismissAlertByTitle removes matching alerts`() = testScope.runTest {
        holder.pushAlert(title = "Match", body = "Body1", importance = 1)
        holder.pushAlert(title = "Other", body = "Body2", importance = 2)
        holder.pushAlert(title = "Match", body = "Body3", importance = 3)

        holder.dismissAlertByTitle("Match")

        val alerts = holder.pendingAlerts.value
        assertEquals(1, alerts.size)
        assertEquals("Other", alerts[0].title)
    }

    @Test
    fun `dismissAlertByTitle does nothing when no match`() = testScope.runTest {
        holder.pushAlert(title = "First", body = "Body", importance = 1)

        holder.dismissAlertByTitle("Nonexistent")

        assertEquals(1, holder.pendingAlerts.value.size)
    }

    @Test
    fun `empty state has no captions or alerts`() {
        assertTrue(holder.recentCaptions.value.isEmpty())
        assertTrue(holder.pendingAlerts.value.isEmpty())
    }
}
