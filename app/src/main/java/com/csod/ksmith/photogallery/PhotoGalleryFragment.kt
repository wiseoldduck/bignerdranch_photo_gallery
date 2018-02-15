package com.csod.ksmith.photogallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.AsyncTask
import kotlinx.android.synthetic.main.fragment_photo_gallery.*
import android.widget.TextView


class PhotoGalleryFragment : Fragment() {

    private var photoRecyclerView: RecyclerView? = null
    private var items: List<GalleryItem> = listOf()

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
        photo_recycler_view?.layoutManager = GridLayoutManager(activity, 3)
    }

    private fun setupAdapter() {
        if (isAdded) {
            photo_recycler_view.adapter = PhotoAdapter(items)
        }
    }

    private inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView as TextView

        fun bindGalleryItem(item: GalleryItem) {
            titleTextView.text = item.toString()
        }
    }

    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>) : RecyclerView.Adapter<PhotoHolder>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PhotoHolder {
            val textView = TextView(activity)
            return PhotoHolder(textView)
        }

        override fun onBindViewHolder(photoHolder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            photoHolder.bindGalleryItem(galleryItem)
        }

        override fun getItemCount(): Int {
            return galleryItems.size
        }
    }

    private inner class FetchItemsTask : AsyncTask<Void, Void, List<GalleryItem>>() {

        override fun doInBackground(vararg params: Void?): List<GalleryItem> {
            return FlickrFetchr().fetchItems()
        }

        /** <p>Runs on the UI thread after {@link #doInBackground}. The
        * specified result is the value returned by {@link #doInBackground}.</p>
        *
        * <p>This method won't be invoked if the task was cancelled.</p>
        *
        * @param result The result of the operation computed by {@link #doInBackground}.
        *
        * @see #onPreExecute
        * @see #doInBackground
        * @see #onCancelled(Object)
        */
        override fun onPostExecute(result: List<GalleryItem>) {
            items = result
            setupAdapter()
        }

    }

    companion object {
        private val     TAG = "PhotoGalleryFragment"
    }
}
