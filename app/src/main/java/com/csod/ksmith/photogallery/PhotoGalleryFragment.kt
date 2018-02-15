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
import android.graphics.drawable.Drawable
import android.widget.ImageView


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

        val imageView = itemView.findViewById(R.id.item_image_view) as? ImageView

        fun bindDrawable(drawable: Drawable) {
            imageView?.setImageDrawable(drawable)
        }

    }

    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>) : RecyclerView.Adapter<PhotoHolder>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PhotoHolder {
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false)
            return PhotoHolder(view)
        }

        override fun onBindViewHolder(photoHolder: PhotoHolder, position: Int) {
            val galleryItem = items.get(position)
            val placeholder = resources.getDrawable(R.drawable.bill_up_close,
                    null)
            photoHolder.bindDrawable(placeholder)
//            mThumbnailDownloader.queueThumbnail(photoHolder, galleryItem.url)

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
