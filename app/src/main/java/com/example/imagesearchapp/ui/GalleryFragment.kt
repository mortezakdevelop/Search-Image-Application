package com.example.imagesearchapp.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.imagesearchapp.R
import com.example.imagesearchapp.adapter.UnSplashPhotoAdapter
import com.example.imagesearchapp.adapter.UnSplashPhotoLoadStateAdapter
import com.example.imagesearchapp.databinding.FragmentGalleryBinding
import com.example.imagesearchapp.viewmodel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment() {
    lateinit var fragmentGalleryBinding: FragmentGalleryBinding
    private val viewModel by viewModels<GalleryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentGalleryBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_gallery, container, false)

        val adapter = UnSplashPhotoAdapter()
        fragmentGalleryBinding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnSplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnSplashPhotoLoadStateAdapter { adapter.retry() }
            )
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
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

}
