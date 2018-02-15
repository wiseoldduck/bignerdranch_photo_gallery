package com.csod.ksmith.photogallery

data class GalleryItem(val caption:String?, val id:String?, val url:String?) {

    override fun toString(): String {
        return caption ?: "null caption"
    }
}