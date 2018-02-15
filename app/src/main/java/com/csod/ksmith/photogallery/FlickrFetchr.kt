package com.csod.ksmith.photogallery

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


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

    @Throws(IOException::class)
    fun getUrlString(urlSpec: String): String {
        return String(getUrlBytes(urlSpec))
    }

}