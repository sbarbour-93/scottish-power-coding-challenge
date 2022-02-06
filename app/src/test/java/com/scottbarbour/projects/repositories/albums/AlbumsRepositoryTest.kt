package com.scottbarbour.projects.repositories.albums

import com.scottbarbour.projects.model.Album
import com.scottbarbour.projects.networking.AlbumsService
import com.scottbarbour.projects.networking.NetworkResult
import io.mockk.coEvery
import io.mockk.mockkClass
import io.mockk.unmockkAll
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declare
import org.koin.test.mock.declareMock
import java.lang.Exception

class AlbumsRepositoryTest : KoinTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    private lateinit var repository: AlbumsRepository
    private lateinit var apiService: AlbumsService

    @Test
    fun `repository wraps successful API response into a successful network response object`() = runBlockingTest {

        val mockAlbumList = listOf(
            Album(1, 1, "Californication"),
            Album(2, 1, "No More Idols"),
            Album(3, 1, "Hotel California")
        )

        apiService = declareMock()
        assertNotNull(apiService)
        coEvery { apiService.getListOfAlbums() } returns mockAlbumList

        repository = declare { AlbumsRepository(apiService, testDispatcher) }
        assertNotNull(repository)

        val result = repository.downloadListOfAlbums()

        assertTrue(result is NetworkResult.Success<List<Album>>)
        assertTrue((result as NetworkResult.Success<List<Album>>).data.isNotEmpty())
        assertTrue(result.data.contains(mockAlbumList[0]))
        assertTrue(result.data.contains(mockAlbumList[1]))
        assertTrue(result.data.contains(mockAlbumList[2]))

    }

    @Test
    fun `repository wraps error API response into a error network response object`() = runBlockingTest {


        apiService = declareMock()
        assertNotNull(apiService)
        val exception = Exception("testing error")
        coEvery { apiService.getListOfAlbums() } throws exception

        repository = declare { AlbumsRepository(apiService, testDispatcher) }
        assertNotNull(repository)

        val result = repository.downloadListOfAlbums()

        assertTrue(result is NetworkResult.Error)
        assertEquals((result as NetworkResult.Error).exception, exception)

    }

    @After
    fun tearDown() {
        unmockkAll()
    }

}