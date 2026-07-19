package com.trio.data.state

import com.trio.data.local.datastore.ModePreferencesDataStore
import com.trio.domain.model.DeviceMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GlobalModeStateHolderTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val holderScope = CoroutineScope(SupervisorJob() + testDispatcher)
    private lateinit var mockDataStore: ModePreferencesDataStore
    private lateinit var stateHolder: GlobalModeStateHolder
    private lateinit var fakeModeFlow: MutableStateFlow<DeviceMode>

    @Before
    fun setup() {
        fakeModeFlow = MutableStateFlow(DeviceMode.STANDARD)
        mockDataStore = mock()
        whenever(mockDataStore.modeFlow).thenReturn(fakeModeFlow)
        runBlocking {
            doAnswer { fakeModeFlow.value = it.arguments[0] as DeviceMode }
                .`when`(mockDataStore)
                .setMode(any())
        }
        stateHolder = GlobalModeStateHolder(mockDataStore, holderScope)
    }

    @After
    fun tearDown() {
        holderScope.cancel()
    }

    @Test
    fun `initial mode is STANDARD`() = testScope.runTest {
        advanceUntilIdle()
        assertEquals(DeviceMode.STANDARD, stateHolder.mode.value)
    }

    @Test
    fun `setMode updates mode through dataStore`() = testScope.runTest {
        advanceUntilIdle()
        stateHolder.setMode(DeviceMode.VISION_IMPAIRED)
        advanceUntilIdle()
        assertEquals(DeviceMode.VISION_IMPAIRED, stateHolder.mode.value)
    }

    @Test
    fun `mode reflects dataStore changes`() = testScope.runTest {
        advanceUntilIdle()
        assertEquals(DeviceMode.STANDARD, stateHolder.mode.value)

        fakeModeFlow.value = DeviceMode.SPEECH_IMPAIRED
        advanceUntilIdle()
        assertEquals(DeviceMode.SPEECH_IMPAIRED, stateHolder.mode.value)
    }

    @Test
    fun `mode transitions through all values`() = testScope.runTest {
        advanceUntilIdle()
        assertEquals(DeviceMode.STANDARD, stateHolder.mode.value)

        DeviceMode.entries.forEach { mode ->
            fakeModeFlow.value = mode
            advanceUntilIdle()
            assertEquals(mode, stateHolder.mode.value)
        }
    }
}
