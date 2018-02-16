 package com.csod.ksmith.photogallery

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap


class ThumbnailDownloader<T>(private val responseHandler: Handler,
                             private val listener:ThumbnailDownloadListener<T>) : HandlerThread(TAG) {

    private var hasQuit = false
    private val requestMap = ConcurrentHashMap<T, String>()

    interface ThumbnailDownloadListener<T> {
        fun onThumbnailDownloaded(target: T, bitmap: Bitmap)
    }

    private class ThumbnailHandler<T>(val outer: WeakReference<ThumbnailDownloader<T>>)
        :Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == MESSAGE_DOWNLOAD) {
                (msg.obj as? T)?.let {
                    Log.i(TAG, "Got a request for URL: " +
                            outer.get()?.requestMap?.get(it))
                    outer.get()?.handleRequest(it)
                }
            }
        }
    }

    override fun onLooperPrepared() {
        requestHandler = ThumbnailHandler(WeakReference(this))
    }

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    fun queueThumbnail(target: T, thumbnailUrl: String?) {

        Log.i(TAG, "Got a URL: " + thumbnailUrl)

        if (thumbnailUrl == null) {
            requestMap.remove(target)
        } else {

            requestHandler?.let {
                requestMap[target] = thumbnailUrl
                it.obtainMessage(MESSAGE_DOWNLOAD, target)
                        .sendToTarget()
            }
        }
    }

    fun clearQueue() {
        requestHandler?.removeMessages(MESSAGE_DOWNLOAD)
        requestMap.clear()
    }

    private fun handleRequest(target: T) {
        try {
            val url = requestMap.get(target) ?: return

            val bitmapBytes = FlickrFetchr().getUrlBytes(url)
            val bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
            Log.i(TAG, "Bitmap created")

            responseHandler.post(Runnable {
                if (requestMap.get(target) !== url || hasQuit) {
                    return@Runnable
                }

                requestMap.remove(target)
                listener?.onThumbnailDownloaded(target, bitmap)
            })
        } catch (ioe: IOException) {
            Log.e(TAG, "Error downloading image", ioe)
        }

    }

    companion object {
        private val TAG = "ThumbnailDownloader"
        private val MESSAGE_DOWNLOAD = 0
    }

    private var requestHandler: Handler? = null
}
