package com.rtgrowth.cockpit

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import com.rtgrowth.cockpit.ui.CockpitDashboardScreen
import com.rtgrowth.cockpit.ui.theme.RTCockpitTheme

class MainActivity : ComponentActivity() {

    private val CHANNEL_ID = "cockpit_live_alerts_channel"

    private var isUsersInitialLoaded = false
    private var isSendMoneyInitialLoaded = false
    private var isChatsInitialLoaded = false

    private val knownDeposits = mutableSetOf<String>()
    private val knownWithdrawals = mutableSetOf<String>()
    private val knownRecharges = mutableSetOf<String>()
    private val knownTasks = mutableSetOf<String>()
    private val knownSendMoney = mutableSetOf<String>()
    private val knownChats = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ১. নোটিফিকেশন চ্যানেল তৈরি
        createNotificationChannel()

        // ২. নোটিফিকেশন পারমিশন চাওয়া (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        // ৩. ফায়ারবেস ডেটাবেস ও লাইভ অ্যালার্ট মনিটরিং শুরু
        initFirebaseAndStartMonitoring()

        setContent {
            RTCockpitTheme {
                CockpitDashboardScreen()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Cockpit Live Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for deposits, cashouts, tasks, and chats"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400, 200, 400)
                setSound(soundUri, audioAttributes)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun triggerNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification_bell) // বেল আইকন
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 400, 200, 400))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun initFirebaseAndStartMonitoring() {
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

            // ১. ডিপোজিট, উইথড্র, রিচার্জ, টাইপিং মনিটরিং
            db.getReference("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (uChild in snapshot.children) {
                        val phone = uChild.key ?: ""
                        val name = uChild.child("name").getValue(String::class.java) ?: phone

                        // ডিপোজিট
                        for (d in uChild.child("deposits").children) {
                            val key = "${phone}_dep_${d.key}"
                            val isPending = d.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownDeposits.contains(key)) {
                                val amt = d.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                                triggerNotification("🔔 New Deposit Request!", "$name ($phone) sent ৳$amt deposit request.")
                            }
                            if (isPending) knownDeposits.add(key)
                        }

                        // উইথড্র
                        for (w in uChild.child("withdrawals").children) {
                            val key = "${phone}_wd_${w.key}"
                            val isPending = w.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownWithdrawals.contains(key)) {
                                val amt = w.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                                triggerNotification("💰 New Cash Out Request!", "$name ($phone) requested cash out of ৳$amt.")
                            }
                            if (isPending) knownWithdrawals.add(key)
                        }

                        // রিচার্জ
                        for (r in uChild.child("recharges").children) {
                            val key = "${phone}_rc_${r.key}"
                            val isPending = r.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownRecharges.contains(key)) {
                                triggerNotification("📱 New Recharge Request!", "$name ($phone) requested mobile recharge.")
                            }
                            if (isPending) knownRecharges.add(key)
                        }

                        // টাইপিং কাজ
                        for (p in uChild.child("paragraph_jobs").children) {
                            val key = "${phone}_pt_${p.key}"
                            val isPending = p.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownTasks.contains(key)) {
                                triggerNotification("📝 New Typing Task!", "$name ($phone) submitted a task for approval.")
                            }
                            if (isPending) knownTasks.add(key)
                        }
                    }
                    isUsersInitialLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            // ২. সেন্ড মানি মনিটরিং
            db.getReference("pending_send_money").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (c in snapshot.children) {
                        val key = "sm_${c.key}"
                        val isPending = c.child("status").getValue(String::class.java) == "Pending"
                        if (isPending && isSendMoneyInitialLoaded && !knownSendMoney.contains(key)) {
                            val sender = c.child("sender").getValue(String::class.java) ?: ""
                            val amt = c.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                            triggerNotification("💸 New Send Money Request!", "$sender requested transfer of ৳$amt.")
                        }
                        if (isPending) knownSendMoney.add(key)
                    }
                    isSendMoneyInitialLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            // ৩. সাপোর্ট চ্যাট মনিটরিং
            db.getReference("chats").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (u in snapshot.children) {
                        val phone = u.key ?: ""
                        for (m in u.child("messages").children) {
                            val key = "chat_${m.key}"
                            val sender = m.child("sender").getValue(String::class.java) ?: ""
                            val seen = m.child("seen").getValue(Boolean::class.java) ?: false
                            val text = m.child("text").getValue(String::class.java) ?: ""
                            if (sender == "user" && !seen && isChatsInitialLoaded && !knownChats.contains(key)) {
                                triggerNotification("💬 New Message from $phone", text)
                            }
                            if (sender == "user" && !seen) knownChats.add(key)
                        }
                    }
                    isChatsInitialLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
