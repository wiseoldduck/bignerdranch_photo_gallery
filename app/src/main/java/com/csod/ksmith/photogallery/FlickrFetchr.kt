package com.csod.ksmith.photogallery

import android.net.Uri
import android.util.Log
import android.widget.Gallery
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject


class FlickrFetchr {
    @Throws(IOException::class)
    fun getUrlBytes(urlSpec: String): ByteArray {
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val outputStream = ByteArrayOutputStream(1024)
            val inputStream = connection.inputStream
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException(connection.responseMessage +
                        ": with " +
                        urlSpec)
            }

            var bytesRead: Int

            val buffer = ByteArray(1024)
            bytesRead = inputStream.read(buffer)
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
            }

            outputStream.close()
            return outputStream.toByteArray()
        } finally {
            connection.disconnect()
        }
    }


    fun fetchItems(): List<GalleryItem> {
        val items = arrayListOf<GalleryItem>()
        try {

            val url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", Private.API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString()
            val jsonString = getUrlString(url)
            Log.i(TAG, "Received JSON: " + jsonString)
            val jsonBody = JSONObject(jsonString)
            parseItems(items, jsonBody)
        } catch (ioe: IOException) {
            Log.e(TAG, "Failed to fetch items", ioe)
        } catch (je: JSONException) {
            Log.e(TAG, "Failed to parse JSON", je)
        }

        return items
    }

    @Throws(IOException::class, JSONException::class)
    private fun parseItems(items: MutableList<GalleryItem>, jsonBody: JSONObject) {

        val photosJsonObject = jsonBody.getJSONObject("photos")
        val photoJsonArray = photosJsonObject.getJSONArray("photo")

        for (i in 0 until photoJsonArray.length()) {
            val photoJsonObject = photoJsonArray.getJSONObject(i)

            val item = GalleryItem(caption = photoJsonObject.getString("title"),
                    id = photoJsonObject.getString("id"),
                    url = photoJsonObject.getString("url_s"))

            items.add(item)
        }
    }

    @Throws(IOException::class)
    private fun getUrlString(urlSpec: String): String {
        return String(getUrlBytes(urlSpec))
    }

    companion object {
        private const val TAG = "FlickrFetchr"
    }

}