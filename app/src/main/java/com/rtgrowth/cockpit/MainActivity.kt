package com.rtgrowth.cockpit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.rtgrowth.cockpit.service.CockpitBackgroundService
import com.rtgrowth.cockpit.ui.CockpitDashboardScreen
import com.rtgrowth.cockpit.ui.theme.RTCockpitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ১. নোটিফিকেশন পারমিশন চাওয়া (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        // ২. ফায়ারবেস ডেটাবেস অনলাইন ও লাইভ সিঙ্ক সচল করা
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

            val db = FirebaseDatabase.getInstance("https://typing-5c3e4-default-rtdb.firebaseio.com")
            db.goOnline()
            db.getReference("users").keepSynced(true)
            db.getReference("pending_send_money").keepSynced(true)
            db.getReference("chats").keepSynced(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // ৩. অ্যাপ বন্ধ থাকলেও ২৪ ঘণ্টা নোটিফিকেশন পাওয়ার জন্য ব্যাকগ্রাউন্ড সার্ভিস চালু
        val serviceIntent = Intent(this, CockpitBackgroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        setContent {
            RTCockpitTheme {
                CockpitDashboardScreen()
            }
        }
    }
}
