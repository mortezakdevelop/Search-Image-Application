package com.example.imagesearchapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.imagesearchapp.R
import com.example.imagesearchapp.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    lateinit var fragmentDitalBinding: FragmentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentDitalBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_detail,container,false)
        return fragmentDitalBinding.root
    }
}