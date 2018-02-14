package com.csod.ksmith.photogallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class PhotoGalleryFragment : Fragment() {

    private var photoRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = v.findViewById(R.id.photo_recycler_view) as RecyclerView
        photoRecyclerView!!.setLayoutManager(GridLayoutManager(activity, 3))

        return v

    }

    companion object {
        private val     TAG = "PhotoGalleryFragment"
    }
}
