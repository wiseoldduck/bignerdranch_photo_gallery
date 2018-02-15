package com.csod.ksmith.photogallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.AsyncTask
import android.util.Log
import kotlinx.android.synthetic.main.fragment_photo_gallery.*
import java.io.IOException


class PhotoGalleryFragment : Fragment() {

    private var photoRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        FetchItemsTask().execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photo_recycler_view?.setLayoutManager(GridLayoutManager(activity, 3))
    }

    private inner class FetchItemsTask : AsyncTask<Void, Void, Int>() {

        override fun doInBackground(vararg params: Void?): Int {
            try {
                val result = FlickrFetchr().getUrlString("https://www.wode.com")
                Log.i(TAG, "Fetched contents of URL: ${result.toString()}")
                return 0
            } catch (ioe: IOException) {
                Log.e(TAG, "Failed to fetch URL: ${ioe.toString()}")
            }
            return 0
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
        }

    }

    companion object {
        private val     TAG = "PhotoGalleryFragment"
    }
}
