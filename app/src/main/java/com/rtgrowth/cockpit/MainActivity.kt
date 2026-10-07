package com.rtgrowth.cockpit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.rtgrowth.cockpit.ui.CockpitDashboardScreen
import com.rtgrowth.cockpit.ui.theme.RTCockpitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ১. ফায়ারবেস কানেকশন ইনিশিয়ালাইজ করা
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApiKey("AIzaSyBgn1GgF4sYq65N4yVxvwxfeU-_WP8lmNs")
                    .setApplicationId("1:334771083987:web:6bba8c6183f9e329d39226")
                    .setDatabaseUrl("https://typing-5c3e4-default-rtdb.firebaseio.com")
                    .setProjectId("typing-5c3e4")
                    .build()
                FirebaseApp.initializeApp(this, options)
            }
            
            // ২. বিকাশ/হোয়াটসঅ্যাপের মতো ক্লাউড নোটিফিকেশন টপিক অন করা
            FirebaseMessaging.getInstance().subscribeToTopic("admin_alerts")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // ৩. নোটিফিকেশন পারমিশন চাওয়া (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        setContent {
            RTCockpitTheme {
                CockpitDashboardScreen()
            }
        }
    }
}
