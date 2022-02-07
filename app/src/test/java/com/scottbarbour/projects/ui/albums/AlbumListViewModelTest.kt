package com.scottbarbour.projects.ui.albums

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.scottbarbour.projects.di.myKoinModules
import com.scottbarbour.projects.model.Album
import com.scottbarbour.projects.networking.NetworkResult
import com.scottbarbour.projects.repositories.albums.AlbumsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import io.mockk.unmockkAll
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declare
import org.koin.test.mock.declareMock

class AlbumListViewModelTest : KoinTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            myKoinModules
        )
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }


    private lateinit var viewModel: AlbumListViewModel
    private lateinit var repository: AlbumsRepository
    private val mockAlbumList = listOf(
        Album(1, 1, "Californication"),
        Album(2, 1, "No More Idols"),
        Album(3, 1, "Hotel California")
    )

    @Test
    fun `when view model constructed the API call to fetch list of albums is called`() {
        repository = declareMock()
        viewModel = get()

        assertNotNull(viewModel)

        coVerify(exactly = 1) { repository.downloadListOfAlbums() }
    }

    @Test
    fun `view model initialised with empty album list live data`() {
        repository = declareMock()
        viewModel = get()

        assertNotNull(viewModel)
        assertTrue(viewModel.albumList.value!!.isEmpty())
    }

    @Test
    fun `view model album list updated when data fetched successfully via repository`() =
        runBlockingTest {

            val mockResponse = NetworkResult.Success(mockAlbumList)

            repository = declareMock()
            coEvery { repository.downloadListOfAlbums() } returns mockResponse

            viewModel = declare { AlbumListViewModel(repository, testDispatcher) }
            assertNotNull(viewModel)

            viewModel.refreshAlbumList()

            assertFalse(viewModel.albumList.value!!.isEmpty())
            assertTrue(viewModel.albumList.value!!.contains(mockAlbumList[0]))
            assertTrue(viewModel.albumList.value!!.contains(mockAlbumList[1]))
            assertTrue(viewModel.albumList.value!!.contains(mockAlbumList[2]))
        }

    @Test
    fun `test albums sorted by title`() {

        viewModel = get()
        assertNotNull(viewModel)

        val result = viewModel.sortAlbumsByTitle(mockAlbumList)

        assertEquals(result[0].title, "Californication")
        assertEquals(result[1].title, "Hotel California")
        assertEquals(result[2].title, "No More Idols")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}