package com.example.android.tvleanback.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.screens.PlaybackScreen

class PlaybackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val video = intent.getParcelableExtra<Video>(VideoDetailsActivity.VIDEO)
        if (video == null) {
            finish()
            return
        }

        setContent {
            PlaybackScreen(
                video = video,
                onFinish = { finish() }
            )
        }
    }
}
