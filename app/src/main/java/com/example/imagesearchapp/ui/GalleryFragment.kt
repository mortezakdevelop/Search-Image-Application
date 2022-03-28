package com.example.imagesearchapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
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
                header = UnSplashPhotoLoadStateAdapter {adapter.retry()},
                footer = UnSplashPhotoLoadStateAdapter {adapter.retry()}
            )
        }

        viewModel.photos.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        return fragmentGalleryBinding.root
    }
}