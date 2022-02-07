package com.scottbarbour.projects

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.scottbarbour.projects.model.Album
import com.scottbarbour.projects.networking.NetworkResult
import com.scottbarbour.projects.repositories.albums.AlbumsRepository
import com.scottbarbour.projects.ui.albums.AlbumListAdapter
import com.scottbarbour.projects.ui.albums.AlbumListFragment
import com.scottbarbour.projects.ui.albums.AlbumListViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module


@RunWith(AndroidJUnit4::class)
class AlbumListFragmentInstrumentedTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun launchFragmentAndVerifyLoadingUiShown() = runBlocking {

        val mockRepository = AlbumsRepository(mockk(), testDispatcher)
        coEvery { mockRepository.downloadListOfAlbums() } returns NetworkResult.Error(Exception("Test Exception"))

        val viewModel = AlbumListViewModel(mockRepository, testDispatcher)
        val mockViewModel = spyk(viewModel)

        every { mockViewModel.isDownloading } returns MutableLiveData(true)
        every { mockViewModel.errorOccurred } returns MutableLiveData(false)

        val testModule = module {
            viewModel { mockViewModel }
        }

        loadKoinModules(testModule)

        launchFragmentInContainer<AlbumListFragment>(themeResId = R.style.Theme_ScottishPowerAlbums)
        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        unloadKoinModules(testModule)
    }

    @Test
    fun launchFragmentAndVerifyErrorUiShown() = runBlocking {

        val repository = AlbumsRepository(mockk(), testDispatcher)
        val mockRepository = spyk(repository)

        coEvery { mockRepository.downloadListOfAlbums() } returns NetworkResult.Error(Exception("Test Exception"))

        val testModule = module {
            viewModel { AlbumListViewModel(mockRepository) }
        }

        loadKoinModules(testModule)

        launchFragmentInContainer<AlbumListFragment>(themeResId = R.style.Theme_ScottishPowerAlbums)
        onView(withId(R.id.error_view)).check(matches(isDisplayed()))

        unloadKoinModules(testModule)
    }

    @Test
    fun launchFragmentAndVerifyListOfAlbumsShown() = runBlocking {

        val repository = AlbumsRepository(mockk(), testDispatcher)
        val mockRepository = spyk(repository)

        val mockAlbumList = listOf(
            Album(1, 1, "Californication"),
            Album(2, 1, "No More Idols"),
            Album(3, 1, "Hotel California")
        )

        coEvery { mockRepository.downloadListOfAlbums() } returns NetworkResult.Success(mockAlbumList)

        val testModule = module {
            viewModel { AlbumListViewModel(mockRepository) }
        }

        loadKoinModules(testModule)

        launchFragmentInContainer<AlbumListFragment>(themeResId = R.style.Theme_ScottishPowerAlbums)
        onView(withId(R.id.album_list)).check(matches(isDisplayed()))

        onView(withId(R.id.album_list))
            .perform(
                RecyclerViewActions.scrollToPosition<AlbumListAdapter.AlbumViewHolder>(0)
            )
            .check(matches(hasDescendant(withText("Californication"))))

        onView(withId(R.id.album_list))
            .check(matches(hasDescendant(withText("No More Idols"))))

        onView(withId(R.id.album_list))
            .check(matches(hasDescendant(withText("Hotel California"))))

        unloadKoinModules(testModule)
    }

    @Test
    fun launchFragmentAndVerifyListOfAlbumsShownSortedByTitle() = runBlocking {

        val repository = AlbumsRepository(mockk(), testDispatcher)
        val mockRepository = spyk(repository)

        val mockAlbumList = listOf(
            Album(1, 1, "Californication"),
            Album(2, 1, "No More Idols"),
            Album(3, 1, "Hotel California")
        )

        coEvery { mockRepository.downloadListOfAlbums() } returns NetworkResult.Success(mockAlbumList)

        val testModule = module {
            viewModel { AlbumListViewModel(mockRepository) }
        }

        loadKoinModules(testModule)

        launchFragmentInContainer<AlbumListFragment>(themeResId = R.style.Theme_ScottishPowerAlbums)
        onView(withId(R.id.album_list)).check(matches(isDisplayed()))

        onView(withId(R.id.album_list))
            .check(matches(atPosition(0, hasDescendant(withText("Californication")))))

        onView(withId(R.id.album_list))
            .check(matches(atPosition(1, hasDescendant(withText("Hotel California")))))

        onView(withId(R.id.album_list))
            .check(matches(atPosition(2, hasDescendant(withText("No More Idols")))))

        unloadKoinModules(testModule)
    }

    /**
     * Recycler ViewMatcher helper
     */
    private fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?>? {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }


}