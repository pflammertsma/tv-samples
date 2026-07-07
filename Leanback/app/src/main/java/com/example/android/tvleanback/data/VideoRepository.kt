@file:Suppress("GlobalCoroutineDispatchers")

package com.example.android.tvleanback.data

import android.content.Context
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.model.VideoCursorMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object VideoRepository {
    suspend fun getVideos(context: Context): List<Video> = withContext(Dispatchers.IO) {
        val cursor = context.contentResolver.query(
            VideoContract.VideoEntry.CONTENT_URI,
            null, null, null, null
        )
        val videos = mutableListOf<Video>()
        cursor?.use {
            val mapper = VideoCursorMapper()
            while (it.moveToNext()) {
                videos.add(mapper.convert(it) as Video)
            }
        }
        videos
    }
}
