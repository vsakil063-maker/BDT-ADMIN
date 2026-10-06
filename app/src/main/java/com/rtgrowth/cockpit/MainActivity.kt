package com.rtgrowth.cockpit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rtgrowth.cockpit.ui.CockpitDashboardScreen
import com.rtgrowth.cockpit.ui.theme.RTCockpitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RTCockpitTheme {
                CockpitDashboardScreen()
            }
        }
    }
}
