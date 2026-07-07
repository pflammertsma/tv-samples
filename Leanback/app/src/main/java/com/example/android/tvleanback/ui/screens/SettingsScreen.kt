package com.example.android.tvleanback.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import com.example.android.tvleanback.R
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val recKey = stringResource(id = R.string.pref_key_recommendations)

    var recommendationsEnabled by remember {
        mutableStateOf(prefs.getBoolean(recKey, true))
    }

    TvLeanbackTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0x80000000)), // Semi-transparent scrim
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(400.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.pref_title_settings),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Recommendations Switch Item
                ListItem(
                    selected = false,
                    onClick = {
                        val newValue = !recommendationsEnabled
                        recommendationsEnabled = newValue
                        prefs.edit().putBoolean(recKey, newValue).apply()
                    },
                    headlineContent = {
                        Text(text = stringResource(id = R.string.pref_title_recommendations))
                    },
                    trailingContent = {
                        Switch(
                            checked = recommendationsEnabled,
                            onCheckedChange = null // Handled by ListItem onClick
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Login Item
                ListItem(
                    selected = false,
                    onClick = onLoginClick,
                    headlineContent = {
                        Text(text = stringResource(id = R.string.pref_title_login))
                    }
                )
            }
        }
    }
}
