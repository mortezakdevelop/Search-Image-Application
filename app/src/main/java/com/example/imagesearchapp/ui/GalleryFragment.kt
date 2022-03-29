package com.example.imagesearchapp.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.imagesearchapp.R
import com.example.imagesearchapp.adapter.UnSplashPhotoAdapter
import com.example.imagesearchapp.adapter.UnSplashPhotoLoadStateAdapter
import com.example.imagesearchapp.databinding.FragmentGalleryBinding
import com.example.imagesearchapp.model.UnSplashPhoto
import com.example.imagesearchapp.viewmodel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(), UnSplashPhotoAdapter.OnItemClickListener {
    lateinit var fragmentGalleryBinding: FragmentGalleryBinding
    private val viewModel by viewModels<GalleryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentGalleryBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_gallery, container, false)

        val adapter = UnSplashPhotoAdapter(this)
        fragmentGalleryBinding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnSplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnSplashPhotoLoadStateAdapter { adapter.retry() }
            )

            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        // when search something, show loading in center of view
        adapter.addLoadStateListener {loadState ->
            fragmentGalleryBinding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached && adapter.itemCount<1){
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                }else{
                    textViewEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)

        return fragmentGalleryBinding.root
    }

    //create search menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.gallery_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fragmentGalleryBinding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })

    }

    override fun onItemClick(photo: UnSplashPhoto) {
        // safe args
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(photo)
        findNavController().navigate(action)
    }

}
