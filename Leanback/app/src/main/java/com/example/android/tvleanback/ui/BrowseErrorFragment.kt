package com.example.android.tvleanback.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.android.tvleanback.ui.screens.ErrorScreen

class BrowseErrorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ErrorScreen(
                    onDismiss = {
                        parentFragmentManager.beginTransaction().remove(this@BrowseErrorFragment).commit()
                        parentFragmentManager.popBackStack()
                    }
                )
            }
        }
    }
}
