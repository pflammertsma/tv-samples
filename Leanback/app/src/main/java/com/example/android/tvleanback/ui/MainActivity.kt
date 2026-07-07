package com.example.android.tvleanback.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.example.android.tvleanback.ui.screens.BrowseScreen

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefs.getBoolean(OnboardingActivity.COMPLETED_ONBOARDING, false)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        setContent {
            BrowseScreen(
                onVideoClick = { video ->
                    val intent = Intent(this, VideoDetailsActivity::class.java).apply {
                        putExtra(VideoDetailsActivity.VIDEO, video)
                    }
                    startActivity(intent)
                },
                onSearchClick = {
                    startActivity(Intent(this, SearchActivity::class.java))
                },
                onSettingsClick = {
                    startActivity(Intent(this, SettingsActivity::class.java))
                },
                onVerticalGridClick = {
                    startActivity(Intent(this, VerticalGridActivity::class.java))
                },
                onGuidedStepClick = {
                    startActivity(Intent(this, GuidedStepActivity::class.java))
                },
                onErrorFragmentClick = {
                    val fragment = BrowseErrorFragment()
                    supportFragmentManager.beginTransaction()
                        .add(android.R.id.content, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            )
        }
    }
}
