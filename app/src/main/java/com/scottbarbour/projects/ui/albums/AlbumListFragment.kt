package com.scottbarbour.projects.ui.albums

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.scottbarbour.projects.R
import com.scottbarbour.projects.databinding.FragmentAlbumListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumListFragment : Fragment() {

    private var _binding: FragmentAlbumListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.album_menu_refresh ->  {
                viewModel.refreshAlbumList()
                return true
            }
        }

        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.swipeRefreshAlbumList.setOnRefreshListener {
            viewModel.refreshAlbumList()
        }
        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        viewModel.albumList.observe(viewLifecycleOwner) { albums ->
            binding.albumList.apply {
                adapter = AlbumListAdapter(albums)
            }
        }

        viewModel.isDownloading.observe(viewLifecycleOwner) { isDownloading ->
            binding.albumList.visibility = if (isDownloading) View.GONE else View.VISIBLE
            binding.swipeRefreshAlbumList.isRefreshing = isDownloading
        }
    }

    private fun setupRecyclerView() {
        binding.albumList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}