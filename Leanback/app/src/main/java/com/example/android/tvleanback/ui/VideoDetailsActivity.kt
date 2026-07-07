package com.example.android.tvleanback.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.screens.VideoDetailsScreen

class VideoDetailsActivity : ComponentActivity() {
    companion object {
        const val VIDEO = "Video"
        const val SHARED_ELEMENT_NAME = "hero"
        const val NOTIFICATION_ID = "NotificationId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val video = intent.getParcelableExtra<Video>(VIDEO)
        if (video == null) {
            finish()
            return
        }

        setContent {
            VideoDetailsScreen(
                video = video,
                onWatchTrailerClick = { v ->
                    val intent = Intent(this, PlaybackActivity::class.java).apply {
                        putExtra(VIDEO, v)
                    }
                    startActivity(intent)
                },
                onRelatedVideoClick = { v ->
                    val intent = Intent(this, VideoDetailsActivity::class.java).apply {
                        putExtra(VIDEO, v)
                    }
                    startActivity(intent)
                }
            )
        }
    }
}
